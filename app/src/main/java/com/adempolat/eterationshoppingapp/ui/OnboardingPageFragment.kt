package com.adempolat.eterationshoppingapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adempolat.eterationshoppingapp.R
import com.adempolat.eterationshoppingapp.databinding.FragmentOnboardingPageBinding

class OnboardingPageFragment : Fragment() {

    private var _binding: FragmentOnboardingPageBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_POSITION = "position"

        fun newInstance(position: Int): OnboardingPageFragment {
            val fragment = OnboardingPageFragment()
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(ARG_POSITION) ?: 0
        when (position) {
            0 -> {
                binding.imageView.setImageResource(R.drawable.ss1)
                binding.textView.text = getString(R.string.onboarding_message_1)
            }
            1 -> {
                binding.imageView.setImageResource(R.drawable.ss2)
                binding.textView.text = getString(R.string.onboarding_message_2)
            }
            2 -> {
                binding.imageView.setImageResource(R.drawable.ss3)
                binding.textView.text = getString(R.string.onboarding_message_3)
            }
            3 -> {
                binding.imageView.setImageResource(R.drawable.ss4)
                binding.textView.text = getString(R.string.onboarding_message_4)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
