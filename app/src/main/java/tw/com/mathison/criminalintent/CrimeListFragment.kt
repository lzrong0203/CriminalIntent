package tw.com.mathison.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import network.TaipeiTourApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import tw.com.mathison.criminalintent.databinding.FragmentCrimeListBinding

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val crimeListViewModel: CrimeListViewModel by viewModels()

    private val BASE_URL =
        "https://www.travel.taipei/"

    private val okHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)

        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)


        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
//            .client(okHttpClient)
            .build()

        val flickrApi: TaipeiTourApi = retrofit.create<TaipeiTourApi>()

        viewLifecycleOwner.lifecycleScope.launch {
            val response = flickrApi.fetchContents()
            Log.d(TAG, "Response received: $response")
        }


        val crimes = crimeListViewModel.crimes
        val adapter = CrimeListAdapter(crimes)
        binding.crimeRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}