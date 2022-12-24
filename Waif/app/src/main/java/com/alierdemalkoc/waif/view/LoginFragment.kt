package com.alierdemalkoc.waif.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.alierdemalkoc.waif.R
import com.alierdemalkoc.waif.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        currentUser?.let {
            val intent = Intent(requireContext(),MainActivity::class.java)
            startActivity(intent)
          /* activity.let {
               val intent = Intent(it,MainActivity::class.java)
               it?.startActivity(intent)
           }*/
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpText.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginToSignUp()
                findNavController().navigate(action)
            }

        binding.signButton.setOnClickListener {
            var email = binding.emailText.text.toString()
            var password = binding.passwordText.text.toString()
            if (TextUtils.isEmpty(email)){
                binding.emailText.error = "Please insert an email!"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(password)){
                binding.passwordText.error = "Please insert your password!"
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(binding.emailText.text.toString(),binding.passwordText.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        activity?.let {
                            val intent = Intent(it,MainActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(requireContext(),"Failed sign in, please retry",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}