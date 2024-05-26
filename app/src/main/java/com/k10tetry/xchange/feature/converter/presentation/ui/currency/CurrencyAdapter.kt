package com.k10tetry.xchange.feature.converter.presentation.ui.currency

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.k10tetry.xchange.databinding.ListItemCurrencyListBinding
import javax.inject.Inject

class CurrencyAdapter @Inject constructor() :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    var countryList = emptyList<Pair<String, String>>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var selectedCurrency: String? = null
    var currencyListener: CurrencyListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): CurrencyViewHolder {
        return CurrencyViewHolder(
            ListItemCurrencyListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(countryList[position])
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    inner class CurrencyViewHolder(private val binding: ListItemCurrencyListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                selectedCurrency?.let { selected ->
                    val oldPosition = countryList.indexOfFirst { it.first == selected }
                    selectedCurrency = countryList[adapterPosition].first
                    notifyItemChanged(oldPosition)
                    notifyItemChanged(adapterPosition)
                }
                currencyListener?.onClick()
            }
        }

        fun bind(currency: Pair<String, String>) {
            binding.textViewName.text = currency.first
            binding.textViewName2.text = currency.second

            selectedCurrency?.let {
                binding.imageViewSelection.visibility =
                    if (currency.first == it) View.VISIBLE else View.GONE
            }
        }

    }

    interface CurrencyListener {
        fun onClick()
    }

}