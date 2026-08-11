package com.example.health_yatra.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.health_yatra.R
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import kotlin.jvm.java

class SignUp : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View? {

            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_sign_up, container, false)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val roles = arrayOf(
            "Patient",
            "Doctor"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            roles
        )

        val role = view.findViewById<MaterialAutoCompleteTextView>(R.id.role)
        role.setAdapter(adapter)



val tvlogin = view.findViewById<TextView>(R.id.tvLogin)

        tvlogin.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.authFragmentContainer,
                    Login()
                )
                .addToBackStack(null)
                .commit()

            (requireActivity() as AppCompatActivity)
                .supportActionBar?.title = "Login"
        }

    }

    }