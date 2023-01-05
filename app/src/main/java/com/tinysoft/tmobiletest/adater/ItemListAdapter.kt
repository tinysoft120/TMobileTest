package com.tinysoft.tmobiletest.adater

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.tinysoft.tmobiletest.GlideApp
import com.tinysoft.tmobiletest.R
import com.tinysoft.tmobiletest.databinding.ItemListCardBinding
import com.tinysoft.tmobiletest.databinding.ItemListCardNoImageBinding
import com.tinysoft.tmobiletest.extensions.parseColor
import com.tinysoft.tmobiletest.network.model.Card

class ItemListAdapter(
    private val activity: FragmentActivity,
    private var dataSet: List<Card> = listOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class NoImageItemViewHolder(val binding: ItemListCardNoImageBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageContainer.setOnClickListener {
                // we can move details screen
            }
        }
    }
    inner class CardItemViewHolder(val binding: ItemListCardBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageContainer.setOnClickListener {
                val item = dataSet[absoluteAdapterPosition]
                /**
                 * we can move Details screen once the item is clicked
                 *
                 * activity.supportFragmentManager.beginTransaction()
                 *     .replace(R.id.container, DetailsFragment.newInstance())
                 *     .commitNow()
                 * activity.findNavController(R.id.container).navigate(
                 *     R.id.container,
                 *     bundleOf(EXTRA_RECIPE_ID to item),
                 * */
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun swapDataSet(dataSet: List<Card>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataSet[position].hasImage) VIEW_TYPE_CARD else VIEW_TYPE_NO_IMAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_CARD) {
            return CardItemViewHolder(ItemListCardBinding.inflate(LayoutInflater.from(activity), parent, false))
        }
        return NoImageItemViewHolder(ItemListCardNoImageBinding.inflate(LayoutInflater.from(activity), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CardItemViewHolder -> {
                val item = dataSet[position]
                with (holder.binding) {
                    title.text = item.title?.value
                    item.title?.attributes?.textColor?.parseColor()?.let {
                        title.setTextColor(it)
                    }
                    item.title?.attributes?.font?.size?.let {
                        title.textSize = it.toFloat()
                    }
                    description.text = item.description?.value
                    item.description?.attributes?.textColor?.parseColor()?.let {
                        description.setTextColor(it)
                    }
                    item.description?.attributes?.font?.size?.let {
                        description.textSize = it.toFloat()
                    }
                    item.image?.let {
                        GlideApp.with(activity).asDrawable()
                            .load(it.url)
                            .into(image)
                        ViewCompat.setTransitionName(image, it.url)

                        val param = image.layoutParams as ConstraintLayout.LayoutParams
                        param.dimensionRatio = "${it.size.width}:${it.size.height}"
                    }
                }
            }
            is NoImageItemViewHolder -> {
                val item = dataSet[position]
                with (holder.binding) {
                    title.text = item.title?.value
                    item.title?.attributes?.textColor?.parseColor()?.let {
                        title.setTextColor(it)
                    }
                    item.title?.attributes?.font?.size?.let {
                        title.textSize = it.toFloat()
                    }
                    description.text = item.description?.value
                    item.description?.attributes?.textColor?.parseColor()?.let {
                        description.setTextColor(it)
                    }
                    item.description?.attributes?.font?.size?.let {
                        description.textSize = it.toFloat()
                    }
                }
            }
        }
    }

    companion object {
        const val VIEW_TYPE_NO_IMAGE = 0
        const val VIEW_TYPE_CARD = 1
    }
}