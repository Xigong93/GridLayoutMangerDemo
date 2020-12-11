package pokercc.android.gridlayoutmangerdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import pokercc.android.gridlayoutmangerdemo.databinding.SampleItemBinding

internal class PracticeResultVH(val binding: SampleItemBinding) :
    RecyclerView.ViewHolder(binding.root)

internal class SampleAdapter(private val resultList: List<Boolean>) :
    RecyclerView.Adapter<PracticeResultVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PracticeResultVH {
        return LayoutInflater.from(parent.context)
            .let { SampleItemBinding.inflate(it, parent, false) }
            .let(::PracticeResultVH)
    }

    override fun getItemCount(): Int = resultList.size

    override fun onBindViewHolder(holder: PracticeResultVH, position: Int) {
        with(holder.binding) {
            val isRight = resultList[position]
            index.text = (position + 1).toString()
            index.setBackgroundResource(
                if (isRight) {
                    R.drawable.rusult_right
                } else {
                    R.drawable.rusult_wrong
                }
            )
            root.setOnClickListener {
                Toast.makeText(it.context, "点击了${position}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}