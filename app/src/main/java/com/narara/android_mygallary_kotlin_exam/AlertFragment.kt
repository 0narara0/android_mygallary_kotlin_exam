package com.narara.android_mygallary_kotlin_exam


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

                    // 인터페이스 대신에 아래 코드, 생성자 추가한 것이라서 null 체크도 안한다.
                    // 생성자에 있는 것들은 field 에 있는 것으로 본다
class AlertFragment(private val onClickListener: () -> Unit) : DialogFragment() {
//    interface onClickListener {
//        fun onClick()
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("권한이 필요한 이유")
        builder.setMessage("사진 정보를 얻으려면 외부 저장소 권한이 필수입니다.")
        builder.setPositiveButton("수락") { _, _ ->
            onClickListener.invoke()
        }
        builder.setNegativeButton("거부", null)
        return builder.create()
    }


}
