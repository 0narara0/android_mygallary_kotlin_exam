package com.narara.android_mygallary_kotlin_exam

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.narara.android_mygallary_kotlin_exam.databinding.ItemPhotoBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    // 글로벌하게 상수 정의 할 때 쓰인다. const val 은 static final 같은 느낌?
    companion object {
        val TAG: String = MainActivity::class.java.simpleName
        const val REQUEST_READ_EXTERNAL_STORAGE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 권한 체크, 권한이 부여되었는지 확인
        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager
                        .PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )
            ) {
                // 이전에 이미 권한이 거부되었을 때 설명
                AlertFragment {
                    // 권한 요청
                    ActivityCompat.requestPermissions(
                            this,
                            // arrayOf는 배열 만드는 것
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            REQUEST_READ_EXTERNAL_STORAGE
                    )
                }.show(supportFragmentManager, "dialog")
            } else {
                // 권한 요청
                ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), // arrayOf는 배열 만드는 것
                        REQUEST_READ_EXTERNAL_STORAGE
                )
            }
        } else {
            // 권한이 이미 허용 됨
            getAllPhotos()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // 권한 허용됨
                    getAllPhotos()
                } else {
                    // 권한 거부
                    toast("권한 거부 됨")
                }
            }
        }
    }


    // 이미지 정보 가져오기
    private fun getAllPhotos() {
        val cursor = contentResolver
                .query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null,
                        null,
                        null,
                        "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC"
                )

        val items = arrayListOf<Photo>()

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                Log.d(TAG, uri.toString())
                items.add(Photo(uri))
            }
            cursor.close()

        }

        val adapter = PhotoAdapter()
        adapter.items = items
        adapter.notifyDataSetChanged()

        view_pager.adapter = adapter


        timer(period = 3000) {
            runOnUiThread {
                if (view_pager.currentItem < adapter.itemCount -1) {
                    view_pager.currentItem = view_pager.currentItem +1
                } else {
                    view_pager.currentItem = 0
                }
            }
        }
    }

}

// Model
data class Photo(val uri: String)

// Adapter
class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
    var items = arrayListOf<Photo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo, parent, false)
        val binding = ItemPhotoBinding.bind(view)
        return PhotoViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.binding.photo = items[position]
    }

    class PhotoViewHolder(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root)

}
