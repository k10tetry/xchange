package com.k10tetry.xchange.feature.converter.presentation.ui.xchange

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.k10tetry.xchange.databinding.GridItemCurrencyRatesBinding
import com.k10tetry.xchange.databinding.ListItemCurrencyRatesBinding
import com.k10tetry.xchange.feature.converter.common.formatRates
import javax.inject.Inject

class XchangeAdapter @Inject constructor() :
    RecyclerView.Adapter<XchangeAdapter.XchangeHolder>() {

    var currencyRateList = emptyList<Pair<String, String>>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var isGridLayout = true

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): XchangeHolder {
        return when {
            viewType == 0 -> {
                XchangeViewXchangeHolder(
                    GridItemCurrencyRatesBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }

            else -> {
                XchangeViewXchangeHolderLinear(
                    ListItemCurrencyRatesBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(xchangeHolder: XchangeHolder, position: Int) {
        when (xchangeHolder) {
            is XchangeViewXchangeHolder -> xchangeHolder.onBind(currencyRateList[position])
            is XchangeViewXchangeHolderLinear -> xchangeHolder.onBind(currencyRateList[position])
        }
    }

    override fun getItemCount(): Int {
        return currencyRateList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGridLayout) 0 else 1
    }

    abstract class XchangeHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class XchangeViewXchangeHolder(private val binding: GridItemCurrencyRatesBinding) :
        XchangeHolder(binding.root) {

        fun onBind(currencyRate: Pair<String, String>) {
            binding.textViewName.text = currencyRate.first
            binding.textViewStatus.text = currencyRate.second.formatRates()
        }

    }

    inner class XchangeViewXchangeHolderLinear(private val binding: ListItemCurrencyRatesBinding) :
        XchangeHolder(binding.root) {

        fun onBind(currencyRate: Pair<String, String>) {
            binding.textViewName.text = currencyRate.first
            binding.textViewStatus.text = currencyRate.second.formatRates()
        }

    }

}