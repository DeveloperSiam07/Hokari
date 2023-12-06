package com.hokari.customer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hokari.customer.R
import com.hokari.customer.database.Database
import com.hokari.customer.databinding.OrderRowBinding
import com.hokari.customer.model.Cart
import com.hokari.customer.ui.activities.CartListActivity
import com.hokari.customer.utils.LoadGlide




open class CartItemAdapter(
    private val context: Context,
    private var itemList: ArrayList<Cart>,
    private val updateCartItems: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class CartItemViewHolder(val binding:OrderRowBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CartItemViewHolder(
            OrderRowBinding.inflate(LayoutInflater.from(context), parent,false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = itemList[position]
        LoadGlide(context).loadProductImage(model.image, holder.binding.iv_cart_item_image)
        holder.binding.tv_cart_item_price.text = "$${model.price}"
        holder.binding.tv_cart_item_title.text = model.title
        holder.binding.tv_cart_quantity.text = model.cart_quantity

        if (model.cart_quantity == "0") {
            holder.binding.ib_remove_cart_item.visibility = View.GONE
            holder.binding.ib_add_cart_item.visibility = View.GONE

            if (updateCartItems) {
                holder.binding.ib_delete_cart_item.visibility = View.VISIBLE
            } else {
                holder.binding.ib_delete_cart_item.visibility = View.GONE
            }

            holder.binding.tv_cart_quantity.text =
                context.resources.getString(R.string.lbl_out_of_stock)

            holder.binding.tv_cart_quantity.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorSnackBarError
                )
            )
        } else {

            if (updateCartItems) {
                holder.binding.ib_remove_cart_item.visibility = View.VISIBLE
                holder.binding.ib_add_cart_item.visibility = View.VISIBLE
                holder.binding.ib_delete_cart_item.visibility = View.VISIBLE
            } else {
                holder.binding.ib_remove_cart_item.visibility = View.GONE
                holder.binding.ib_add_cart_item.visibility = View.GONE
                holder.binding.ib_delete_cart_item.visibility = View.GONE
            }



            holder.itemView.tv_cart_quantity.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorSecondaryText
                )
            )
        }

        holder..ib_delete_cart_item.setOnClickListener {
            when (context) {
                is CartListActivity -> {
                    context.showProgressBar(context.getString(R.string.please_wait))
                }
            }
            Database().removeItemInCart(context, model.id)

        }

        holder.itemView.ib_remove_cart_item.setOnClickListener {
            if (model.cart_quantity == "1") {
                Database().removeItemInCart(context, model.id)
            } else {
                val cartQuantity: Int = model.cart_quantity.toInt()
                val itemHashMap = HashMap<String, Any>()
                itemHashMap["cart_quantity"] = (cartQuantity - 1).toString()
                if (context is CartListActivity) {
                    context.showProgressBar(context.getString(R.string.please_wait))
                }
                Database().updateCartList(context, model.id, itemHashMap)
            }
        }

        holder.itemView.ib_add_cart_item.setOnClickListener {
            val cartQuantity: Int = model.cart_quantity.toInt()
            if (cartQuantity < model.stock_quantity.toInt()) {
                val itemHashMap = HashMap<String, Any>()
                itemHashMap["cart_quantity"] = (cartQuantity + 1).toString()

                if (context is CartListActivity) {
                    context.showProgressBar(context.getString(R.string.please_wait))
                }
                Database().updateCartList(context, model.id, itemHashMap)
            } else {
                if (context is CartListActivity) {
                    val stringForStock: String =
                        context.getString(R.string.msg_for_available_stock, model.stock_quantity)
                    Toast.makeText(context, stringForStock, Toast.LENGTH_LONG).show()
                }
            }

        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }


}