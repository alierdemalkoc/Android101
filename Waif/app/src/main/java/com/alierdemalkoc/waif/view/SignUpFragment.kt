package com.alierdemalkoc.waif.view

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.alierdemalkoc.waif.R
import com.alierdemalkoc.waif.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference.child("Profile")


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpButton.setOnClickListener {
            var nameAndSurname = binding.nameSurnameText.text.toString()
            var newPassword = binding.newPasswordText.text.toString()
            var newEmail = binding.newEmailText.text.toString()
            if (TextUtils.isEmpty(nameAndSurname)) {
                binding.nameSurnameText.error = "Please insert name and surname!"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(newPassword)) {
                binding.newPasswordText.error = "Please insert password"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(newEmail)) {
                binding.newEmailText.error = "please insert email"
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(binding.newEmailText.text.toString(), binding.newPasswordText.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("esad", "createUserWithEmail:success")
                        var currentUser = auth.currentUser
                        var currentUserDb = currentUser?.let { databaseReference.child(it.uid) }
                        currentUserDb?.child("nameAndSurname")?.setValue(binding.nameSurnameText.text.toString())
                        Toast.makeText(requireContext(),"Succes Sign Up",Toast.LENGTH_SHORT).show()
                        val action = SignUpFragmentDirections.actionSignUpToLogin()
                        findNavController().navigate(action)

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("esad", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            requireContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }


    }

   // fun call(){}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}