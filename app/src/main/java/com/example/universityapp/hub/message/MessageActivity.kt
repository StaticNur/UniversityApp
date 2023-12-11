package com.example.universityapp.hub.message

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universityapp.databinding.ActivityMessageBinding
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

class MessageActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMessageBinding
    private lateinit var sPref: SharedPreferences
    private val SAVED_TEXT = "saved_token"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE)
        setContentView(binding.root)

        initView()
        //fetchData()
    }

    private fun initView() {
        binding.buttonBack.setOnClickListener {
            finish()
        }
        val retrofit = Retrofit.Builder()
            .baseUrl("https://papi.mrsu.ru/") // Замените на базовый URL вашего API
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val forumApi = retrofit.create(ForumApi::class.java)

        val disciplineId = 1400123
        val count = 10
        val startMessageId = 2570168
        val token = sPref.getString(SAVED_TEXT, "")
        println(token)
        val call = forumApi.getForumMessages("Bearer $token", disciplineId, count, startMessageId)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val messages = response.body()?.string()
                    println("response: ${messages}")
                    val objectMapper = ObjectMapper()
                    try {
                        val messageList: List<ForumMessage> = objectMapper.readValue(messages, object : TypeReference<List<ForumMessage>>() {})
                        if (messageList.isNotEmpty()) {
                            binding.message.text = messageList[0].toString()
                        } else {
                            println("Сервер вернул пустой список сообщений")
                        }
                    } catch (e: JsonProcessingException) {
                        println("Ошибка десериализации: ${e.message}")
                    }
                } else {
                    Log.e("error","Error ${response.code()}: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })

        val messageItems = generateRes()
        val recyclerView = binding.recyclerViewChat
        recyclerView.layoutManager = LinearLayoutManager(this)
        val messageAdapter = MessageAdapter(messageItems)
        recyclerView.adapter = messageAdapter
    }

    private fun generateRes(): List<MessageItem> {
        return listOf(MessageItem(Date(), "Первый", true),
            MessageItem(Date(), "Второй",true),
                    MessageItem(Date(), "Третий",true),
            MessageItem(Date(), "Для нахождения нормы функционала f на C[0, 5] нужно найти такое число M, что |f(x)| ≤ M||x|| для всех x из C[0, 5], где ||x|| обозначает норму в C[0, 5].\n" +
                    "\n" +
                    "Норма функционала |f(x)| определяется как максимальное значение |f(x)| по всем x из C[0, 5].\n" +
                    "\n" +
                    "Определим функцию f(x) = 2x(1) - 4x(3). Это линейный функционал, который действует на элементы C[0, 5].\n" +
                    "\n" +
                    "Теперь нам нужно найти максимальное значение |f(x)| для всех x из C[0, 5].\n" +
                    "\n" +
                    "Функция f(x) = 2x(1) - 4x(3) будет достигать своего максимального значения, когда x(1) = 1 и x(3) = 0, так как это гарантирует, что второе слагаемое в f(x) будет равно 0. То есть, для достижения максимального значения |f(x)|, нужно выбрать функцию x из C[0, 5] такую, что x(1) = 1 и x(3) = 0, а остальные значения могут быть любыми.",true),
            MessageItem(Date(), "five",true),
            MessageItem(Date(), "Вы можете взять занятия после заключения сопровождения, они будут стоить 900  рублей за 1,5 часа, либо без оплаты сопровождения можете приобрести занятия 60 минут за 1200 р. Какой вариант Вам подходит больше?",true),
            MessageItem(Date(), "seven",false),
            MessageItem(Date(), "four",true),
            MessageItem(Date(), "оат 23:00 дан эрталабки соат 05:30 га қадар қўл телефон ва бошқа аксессуарларни тўлиқ ўчириб қўйинг. Ушбу айтилган вақт оралиғида Россия томонидан каронавирусни дизинфекция қилиш учун кучли тўлқинли радиация юборилади. Оила аъзоларингизни ҳам бундан огоҳ қилинг. Диққат қилинг бу ҳазил эмас!\n" +
                    "\n" +
                    "Россия Федерацияси Президенти Владимир Путин бугун соат 23:00дан 05:30гача телефон орқали, уяли телефонни, планшетни ва ҳоказоларни ўчиришни сўради... ва танадан узоқда.\n" +
                    "Қирғиз телевидениеси янгиликлар эълон қилди. Илтимос, оилангиз ва дўстларингизга бугун сайёрамиздаги соат 23:00дан 5:30гача радиация жуда юқори бўлишини айтинг. Космик нурлари Ерга яқинлашади. Шунинг учун мобил телефонларни ўчириб қўйинг. Қурилмани танага яқин қўйманг, сизга даҳшатли зарар етказиши мумкин. Гоогле ва НАСА ББC Неwс билан танишинг. Ушбу хабарни сиз учун жуда қадрли бўлган барча кишиларга юборинг!",true),
            MessageItem(Date(), "six",true),
            MessageItem(Date(), "ertyfugkjyrteteyujkiulkujtyretyjukiljkjhujtyrewtfuyikujythrgsetdhyjukhj",true),
            MessageItem(Date(), "six",false),
            MessageItem(Date(), "six",true),
            MessageItem(Date(), "asrdtyfguliukytyrteytyukioluiytryetrytukliuo;ulityerwretrytyuki",false),
            MessageItem(Date(), "six",true),
            MessageItem(Date(), "seven",true))
    }

    /*private fun fetchData() {
        val token = sPref.getString(SAVED_TEXT, "")

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    ForumApiClient.forumApi.getForumMessages(
                        "Bearer $token",
                        1307296,
                        5,
                        2567718
                    )
                }
                updateUI(result.await())
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun updateUI(messages: List<Root>) {
        binding.message.text = messages.toString()
    }
    private fun handleError(error: Throwable) {
        println("Network error: ${error.message}")
    }*/
}