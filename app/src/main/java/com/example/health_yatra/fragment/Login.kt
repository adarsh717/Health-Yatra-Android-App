package com.example.health_yatra.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.health_yatra.R

class Login : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val registerBtn = view.findViewById<TextView>(R.id.tvRegister)

registerBtn.setOnClickListener {

    parentFragmentManager.popBackStack(
        null,
        androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
    )


    parentFragmentManager.beginTransaction()
        .replace(
            R.id.authFragmentContainer,
            SignUp()
        )
        .addToBackStack(null)
        .commit()


}

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
    }
