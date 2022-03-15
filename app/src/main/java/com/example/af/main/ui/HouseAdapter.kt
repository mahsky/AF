package com.example.af.main.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.af.framework.net.Network
import com.af.model.Price
import com.bumptech.glide.Glide
import com.example.af.databinding.ItemHouseBinding
import com.example.af.db.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by mah on 2020-01-16.
 */
class HouseAdapter(
    private val orders: MutableList<House>,
    private val delete: (house: House) -> Unit,
    private val remark: (house: House) -> Unit
) : RecyclerView.Adapter<HouseRender>() {
    @SuppressLint("NotifyDataSetChanged")
    fun setHouse(orders: List<House>) {
        this.orders.clear()
        this.orders.addAll(orders)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseRender {
        val viewBinding = ItemHouseBinding.inflate(LayoutInflater.from(parent.context))
        return HouseRender(viewBinding, delete, remark)
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: HouseRender, position: Int) {
        holder.render(orders[position], position)
    }
}

class HouseRender(
    private val viewBinding: ItemHouseBinding,
    private val delete: (house: House) -> Unit,
    private val remark: (house: House) -> Unit
) : RecyclerView.ViewHolder(viewBinding.root) {

    val onTouchListener: View.OnTouchListener = object : View.OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            if (MotionEvent.ACTION_DOWN == event.action) {
                v.getParent().requestDisallowInterceptTouchEvent(true)
            } else if (MotionEvent.ACTION_UP == event.action) {
                v.getParent().requestDisallowInterceptTouchEvent(false)
            }
            return false
        }
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    fun render(house: House, position: Int) {
        viewBinding.title.text = (position + 1).toString() + " " + house.title + " " + house.id
        viewBinding.text.text = "挂牌时间 ${house.guaPaiTime}  ${house.size} ${house.other1}"
        viewBinding.price.text = getPrice(house.priceJson)
        viewBinding.price.movementMethod = ScrollingMovementMethod.getInstance()
        viewBinding.remarkText.text = house.other2
        viewBinding.remarkText.isVisible = !house.other2.isNullOrEmpty()
        viewBinding.xiajiaStatus.isVisible = HOUSE_LIST_XIAJIA == house.other3
        Glide.with(viewBinding.img).load(house.img).into(viewBinding.img)
//        viewBinding.price.setOnTouchListener(onTouchListener)


        when (house.priceStatus) {
            HOUSE_PRICE_STAUS_NONE -> {
                viewBinding.priceStatus.text = ""
            }
            HOUSE_PRICE_STAUS_UP -> {
                viewBinding.priceStatus.text = "上升"
                viewBinding.priceStatus.setTextColor(viewBinding.root.context.resources.getColor(android.R.color.holo_red_light))
            }
            HOUSE_PRICE_STAUS_DOWN -> {
                viewBinding.priceStatus.text = "下降"
                viewBinding.priceStatus.setTextColor(viewBinding.root.context.resources.getColor(android.R.color.holo_green_light))
            }
        }

        viewBinding.root.setOnClickListener {
            val uri: Uri = Uri.parse("https://bj.lianjia.com/ershoufang/${house.id}.html")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(viewBinding.root.context, intent, null)
        }
        viewBinding.root.setOnLongClickListener {
            delete.invoke(house)
            true
        }
        viewBinding.remark.setOnClickListener {
            remark.invoke(house)
        }
    }

    private fun getPrice(priceJson: String?): CharSequence {
        if (priceJson.isNullOrEmpty()) return ""
        val adapter: JsonAdapter<List<Price>> =
            Network.moshi.adapter(Types.newParameterizedType(List::class.java, Price::class.java))
        val priceList = adapter.fromJson(priceJson) ?: return ""
        val sb = StringBuilder()
        sb.append("<ul>")
        priceList.forEachIndexed { index, price ->
            sb.append(convertLongToTime(price.time))
            sb.append("  ")
            if (index == priceList.size - 1) {
                sb.append("<font color='#FF6347'>")
            }
            sb.append(price.price)
            if (index == priceList.size - 1) {
                sb.append("</font>")
            }
            sb.append("万")
            sb.append("<br/>")
        }
        sb.append("</ul>")
        return Html.fromHtml(sb.toString())
    }

    @SuppressLint("SimpleDateFormat")
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd")
        return format.format(date)
    }
}