package reed.flyingreed.model

import android.support.v7.widget.RecyclerView
import reed.flyingreed.mvvm.ViewModel

/**
 * Created by thinkreed on 2017/6/17.
 */

class ViewHolder<T>(val viewModel: ViewModel<T>) : RecyclerView.ViewHolder(viewModel.rootView)