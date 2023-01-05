package com.tinysoft.tmobiletest.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.res.ResourcesCompat
import com.tinysoft.tmobiletest.R
import com.tinysoft.tmobiletest.databinding.ProgressDialogViewBinding

class CustomProgressDialog {

    private var dialog: CustomDialog? = null
    val isShowing: Boolean get() = dialog?.isShowing == true

    fun show(context: Context): Dialog {
        return show(context, null)
    }

    fun show(context: Context, title: CharSequence?): Dialog {
        val inflater = (context as Activity).layoutInflater
        val binding = ProgressDialogViewBinding.inflate(inflater)
        if (title != null) {
            binding.cpTitle.text = title
        }

        // Card Color
        binding.cpCardview.setCardBackgroundColor(Color.parseColor("#90424242"))

        // Progress Bar Color
        setColorFilter(binding.cpPbar.indeterminateDrawable, ResourcesCompat.getColor(context.resources, R.color.md_white_1000, null))

        // Text Color
        binding.cpTitle.setTextColor(Color.WHITE)

        val dlg = CustomDialog(context)
        dlg.setContentView(binding.root)
        dlg.show()
        this.dialog = dlg
        return dlg
    }

    fun dismiss() = dialog?.dismiss()

    private fun setColorFilter(drawable: Drawable, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    class CustomDialog(context: Context) : Dialog(context, R.style.CustomDialogTheme) {
        init {
            // Set Semi-Transparent Color for Dialog Background
            window?.decorView?.rootView?.setBackgroundResource(R.color.progress_background)
            window?.decorView?.setOnApplyWindowInsetsListener { _, insets ->
                insets.consumeSystemWindowInsets()
            }
        }
    }
}