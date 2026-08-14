package com.example.health_yatra.fragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.health_yatra.R
import com.google.android.material.textfield.MaterialAutoCompleteTextView

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
            "Select Role",
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

//          clear previous stack record
            parentFragmentManager.popBackStack(
                null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
            )

            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.authFragmentContainer,
                    Login()
                )
                .addToBackStack("Login")
                .commit()

            (requireActivity() as AppCompatActivity)
                .supportActionBar?.title = "Login"
        }
    }
}