package com.jcdc.newstationsofthecross.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jcdc.newstationsofthecross.R
import com.jcdc.newstationsofthecross.data.model.StationGridItem

class StationAdapter(
    private var items: List<StationGridItem>,
    private var completedTitles: Set<String> = emptySet(),
    private val onItemClick: (StationGridItem, View) -> Unit
) : RecyclerView.Adapter<StationAdapter.StationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_station, parent, false)
        return StationViewHolder(view)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.bind(items[position], completedTitles, onItemClick)
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<StationGridItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun updateCompletedTitles(newTitles: Set<String>) {
        completedTitles = newTitles
        notifyDataSetChanged()
    }

    class StationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivStation: android.widget.ImageView = itemView.findViewById(R.id.iv_station)
        private val tvNumber: TextView = itemView.findViewById(R.id.tv_station_number)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_station_title)
        private val ivDone: android.widget.ImageView = itemView.findViewById(R.id.iv_done)

        fun bind(item: StationGridItem, completedTitles: Set<String>, onItemClick: (StationGridItem, View) -> Unit) {
            val isCompleted = completedTitles.contains(item.title)
            ivDone.visibility = if (isCompleted) View.VISIBLE else View.GONE

            when (item) {
                is StationGridItem.IntroductionItem -> {
                    ivStation.setImageResource(R.drawable.conclusion)
                    ivStation.visibility = View.VISIBLE
                    ivStation.transitionName = "station_image_intro"

                    tvNumber.text = "Introduction"
                    tvTitle.text = item.title
                }
                is StationGridItem.StationItem -> {
                    val context = itemView.context
                    val resourceId = context.resources.getIdentifier(
                        "station_${item.station.id}",
                        "drawable",
                        context.packageName
                    )
                    if (resourceId != 0) {
                        ivStation.setImageResource(resourceId)
                    } else {
                        ivStation.setImageDrawable(null)
                    }
                    ivStation.visibility = View.VISIBLE
                    ivStation.transitionName = "station_image_${item.station.id}"

                    tvNumber.text = item.station.id.toString()
                    tvTitle.text = item.station.title.replace("Ang Unang Istasyon: ", "")
                        .replace("Ang Ikalawang Istasyon: ", "")
                        .replace("Ang Ikatlong Istasyon: ", "")
                        .replace("Ang Ikaapat na Istasyon: ", "")
                        .replace("Ang Ikalimang Istasyon: ", "")
                        .replace("Ang Ikaanim na Istasyon: ", "")
                        .replace("Ang Ikapitong Istasyon: ", "")
                        .replace("Ang Ikawalong Istasyon: ", "")
                        .replace("Ang Ikasiyam na Istasyon: ", "")
                        .replace("Ang Ikasampung Istasyon: ", "")
                        .replace("Ang Ikalabing-isang Istasyon: ", "")
                        .replace("Ang Ikalabing-dalawang Istasyon: ", "")
                        .replace("Ang Ikalabing-tatlong Istasyon: ", "")
                        .replace("Ang Ikalabing-apat na Istasyon: ", "")
                }
                is StationGridItem.ConclusionItem -> {
                    val context = itemView.context
                    ivStation.setImageResource(R.drawable.conclusion)
                    ivStation.visibility = View.VISIBLE
                    ivStation.transitionName = "station_image_conclusion"

                    tvNumber.text = "Conclusion"
                    tvTitle.text = item.conclusion.title
                }
            }
            itemView.setOnClickListener { onItemClick(item, ivStation) }
        }
    }
}
