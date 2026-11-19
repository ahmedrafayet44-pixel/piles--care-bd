package com.pilescarebd.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PilesCareApp()
            }
        }
    }
}

data class FoodItem(
    val id: String,
    val name: String,
    val calories: Double,
    val fiber: Double,
    val protein: Double
)

data class FoodEntry(
    val id: String,
    val name: String,
    val meal: String,
    val amount: Double,
    val unit: String,
    val grams: Double,
    val calories: Double,
    val fiber: Double,
    val protein: Double
)

private fun food(
    id: String,
    name: String,
    calories: Double,
    fiber: Double,
    protein: Double
) = FoodItem(id, name, calories, fiber, protein)

// পুষ্টির মান প্রতি ১০০ গ্রামের আনুমানিক হিসাব
private val foodDatabase = listOf(

    // ফল - ৪০টি
    food("f01","আম",60.0,1.6,0.8),
    food("f02","কলা",89.0,2.6,1.1),
    food("f03","পেয়ারা",68.0,5.4,2.6),
    food("f04","আপেল",52.0,2.4,0.3),
    food("f05","কমলা",47.0,2.4,0.9),
    food("f06","মাল্টা",47.0,2.4,0.9),
    food("f07","পেঁপে",43.0,1.7,0.5),
    food("f08","কাঁঠাল",95.0,1.5,1.7),
    food("f09","লিচু",66.0,1.3,0.8),
    food("f10","জাম",62.0,0.0,0.7),
    food("f11","কালোজাম",62.0,0.0,0.7),
    food("f12","আনারস",50.0,1.4,0.5),
    food("f13","তরমুজ",30.0,0.4,0.6),
    food("f14","বাঙ্গি",34.0,0.9,0.8),
    food("f15","ডালিম",83.0,4.0,1.7),
    food("f16","নাশপাতি",57.0,3.1,0.4),
    food("f17","আঙুর",69.0,0.9,0.7),
    food("f18","ড্রাগন ফল",57.0,3.0,1.2),
    food("f19","খেজুর",282.0,8.0,2.5),
    food("f20","কিশমিশ",299.0,3.7,3.1),
    food("f21","নারকেল",354.0,9.0,3.3),
    food("f22","আমড়া",45.0,3.0,0.5),
    food("f23","আমলকি",44.0,4.3,0.9),
    food("f24","জামরুল",25.0,0.6,0.6),
    food("f25","বেল",137.0,2.9,1.8),
    food("f26","তাল",87.0,0.0,0.5),
    food("f27","লাল পেয়ারা",68.0,5.4,2.6),
    food("f28","স্ট্রবেরি",32.0,2.0,0.7),
    food("f29","কিউই",61.0,3.0,1.1),
    food("f30","পিচ ফল",39.0,1.5,0.9),
    food("f31","বরই",79.0,4.0,1.2),
    food("f32","তেঁতুল",239.0,5.1,2.8),
    food("f33","লেবু",29.0,2.8,1.1),
    food("f34","জলপাই",115.0,3.2,0.8),
    food("f35","পাকা পেঁপে",43.0,1.7,0.5),
    food("f36","কাঁচা আম",60.0,1.6,0.8),
    food("f37","অ্যাভোকাডো",160.0,6.7,2.0),
    food("f38","চেরি",63.0,2.1,1.1),
    food("f39","আলুবোখারা",46.0,1.4,0.7),
    food("f40","শুকনো ডুমুর",249.0,9.8,3.3),

    // শাকসবজি - ৬০টি
    food("v01","পালং শাক",23.0,2.2,2.9),
    food("v02","লাল শাক",23.0,2.0,2.5),
    food("v03","পুঁইশাক",19.0,2.0,1.8),
    food("v04","কলমি শাক",30.0,2.1,3.0),
    food("v05","ডাঁটা শাক",23.0,2.0,2.0),
    food("v06","লাউ",14.0,1.0,0.6),
    food("v07","মিষ্টিকুমড়া",26.0,0.5,1.0),
    food("v08","চালকুমড়া",13.0,0.5,0.4),
    food("v09","করলা",17.0,2.6,1.0),
    food("v10","ঢেঁড়স",33.0,3.2,1.9),
    food("v11","পটল",20.0,2.0,1.0),
    food("v12","ঝিঙে",20.0,1.0,1.2),
    food("v13","চিচিঙ্গা",18.0,1.0,0.8),
    food("v14","বেগুন",25.0,3.0,1.0),
    food("v15","টমেটো",18.0,1.2,0.9),
    food("v16","গাজর",41.0,2.8,0.9),
    food("v17","শসা",15.0,0.5,0.7),
    food("v18","ফুলকপি",25.0,2.0,1.9),
    food("v19","বাঁধাকপি",25.0,2.5,1.3),
    food("v20","ব্রকলি",34.0,2.6,2.8),
    food("v21","আলু",87.0,1.8,1.9),
    food("v22","মিষ্টি আলু",86.0,3.0,1.6),
    food("v23","কাঁচা পেঁপে",39.0,1.8,0.6),
    food("v24","কাঁচা কলা",122.0,2.3,1.3),
    food("v25","কাঁচা কাঁঠাল",95.0,1.5,1.7),
    food("v26","মুলা",16.0,1.6,0.7),
    food("v27","শালগম",28.0,1.8,0.9),
    food("v28","বিট",43.0,2.8,1.6),
    food("v29","শিম",47.0,3.4,2.8),
    food("v30","বরবটি",47.0,3.0,2.8),
    food("v31","মটরশুঁটি",81.0,5.7,5.4),
    food("v32","কাঁচা মরিচ",40.0,1.5,2.0),
    food("v33","ক্যাপসিকাম",31.0,2.1,1.0),
    food("v34","পেঁয়াজ",40.0,1.7,1.1),
    food("v35","রসুন",149.0,2.1,6.4),
    food("v36","আদা",80.0,2.0,1.8),
    food("v37","ধনেপাতা",23.0,2.8,2.1),
    food("v38","পুদিনাপাতা",44.0,6.8,3.3),
    food("v39","লেটুস",15.0,1.3,1.4),
    food("v40","সজনে ডাঁটা",37.0,2.0,2.1),
    food("v41","সজনে পাতা",64.0,2.0,9.4),
    food("v42","কচুর লতি",42.0,3.0,1.5),
    food("v43","কচু",112.0,4.1,1.5),
    food("v44","ওল",98.0,3.0,1.5),
    food("v45","কাঁকরোল",17.0,2.0,1.0),
    food("v46","কুমড়ার ফুল",15.0,1.0,1.0),
    food("v47","মাশরুম",22.0,1.0,3.1),
    food("v48","বাঁশের কুঁড়ি",27.0,2.2,2.6),
    food("v49","পেঁয়াজকলি",32.0,2.6,1.8),
    food("v50","শালুক",80.0,2.0,2.0),
    food("v51","পদ্মের ডাঁটা",74.0,4.9,2.6),
    food("v52","কাঁচা বরবটি",47.0,3.0,2.8),
    food("v53","কাঁচা মটর",81.0,5.7,5.4),
    food("v54","কাঁচা গাজর",41.0,2.8,0.9),
    food("v55","সেলারি",14.0,1.6,0.7),
    food("v56","জুকিনি",17.0,1.0,1.2),
    food("v57","লাল ক্যাপসিকাম",31.0,2.1,1.0),
    food("v58","হলুদ ক্যাপসিকাম",27.0,1.9,1.0),
    food("v59","কাঁচা টমেটো",18.0,1.2,0.9),
    food("v60","বেবি কর্ন",26.0,2.0,2.0),

    // চাল, শস্য ও রুটি - ২০টি
    food("g01","সাদা ভাত",130.0,0.4,2.7),
    food("g02","লাল চালের ভাত",123.0,1.8,2.7),
    food("g03","আটার রুটি",297.0,10.7,11.0),
    food("g04","পরোটা",326.0,3.0,7.0),
    food("g05","লুচি",330.0,2.0,6.0),
    food("g06","চিড়া",350.0,2.0,7.0),
    food("g07","মুড়ি",402.0,1.0,6.0),
    food("g08","খই",370.0,1.0,7.0),
    food("g09","ওটস",389.0,10.6,16.9),
    food("g10","সুজি",360.0,3.9,12.7),
    food("g11","ভুট্টা",96.0,2.7,3.4),
    food("g12","যব",354.0,17.3,12.5),
    food("g13","গম",339.0,12.2,13.2),
    food("g14","বাজরা",378.0,8.5,11.0),
    food("g15","রাগি",336.0,3.6,7.3),
    food("g16","চালের আটা",366.0,2.4,6.0),
    food("g17","গমের আটা",340.0,10.7,13.2),
    food("g18","পাউরুটি",265.0,2.7,9.0),
    food("g19","বিস্কুট",450.0,2.0,7.0),
    food("g20","নুডলস",138.0,1.2,4.5),

    // ডাল ও শিমজাতীয় - ১৫টি
    food("d01","মসুর ডাল রান্না",116.0,7.9,9.0),
    food("d02","মুগ ডাল রান্না",105.0,7.6,7.0),
    food("d03","মাসকলাই ডাল",105.0,7.5,7.5),
    food("d04","ছোলা সেদ্ধ",164.0,7.6,8.9),
    food("d05","কাবুলি ছোলা",164.0,7.6,8.9),
    food("d06","কালো ছোলা",164.0,7.6,8.9),
    food("d07","রাজমা",127.0,6.4,8.7),
    food("d08","সয়াবিন",173.0,6.0,16.6),
    food("d09","মটর ডাল",118.0,8.3,8.3),
    food("d10","অড়হর ডাল",111.0,6.7,7.2),
    food("d11","মটরশুঁটির ডাল",118.0,8.3,8.3),
    food("d12","মটর সেদ্ধ",84.0,5.5,5.4),
    food("d13","টোফু",76.0,0.3,8.0),
    food("d14","সয়াবিন বড়ি",345.0,13.0,52.0),
    food("d15","ইসবগুলের ভুসি",200.0,80.0,2.0),

    // মাছ ও সামুদ্রিক খাবার - ২০টি
    food("m01","রুই মাছ",127.0,0.0,17.8),
    food("m02","কাতলা মাছ",130.0,0.0,17.0),
    food("m03","ইলিশ মাছ",310.0,0.0,18.0),
    food("m04","পাঙ্গাস মাছ",143.0,0.0,15.0),
    food("m05","তেলাপিয়া মাছ",128.0,0.0,26.0),
    food("m06","কই মাছ",110.0,0.0,16.0),
    food("m07","শিং মাছ",105.0,0.0,17.0),
    food("m08","মাগুর মাছ",95.0,0.0,16.0),
    food("m09","পুঁটি মাছ",100.0,0.0,17.0),
    food("m10","টেংরা মাছ",100.0,0.0,17.0),
    food("m11","মলা মাছ",110.0,0.0,18.0),
    food("m12","কৈ মাছ",110.0,0.0,16.0),
    food("m13","চিংড়ি",99.0,0.0,24.0),
    food("m14","কাঁকড়া",87.0,0.0,18.0),
    food("m15","শুঁটকি মাছ",290.0,0.0,55.0),
    food("m16","সার্ডিন মাছ",208.0,0.0,24.6),
    food("m17","টুনা মাছ",132.0,0.0,28.0),
    food("m18","স্যামন মাছ",208.0,0.0,20.0),
    food("m19","ম্যাকেরেল মাছ",205.0,0.0,19.0),
    food("m20","কোরাল মাছ",110.0,0.0,20.0),

    // মাংস, ডিম ও দুধ - ১৫টি
    food("p01","মুরগির মাংস",165.0,0.0,31.0),
    food("p02","গরুর মাংস",250.0,0.0,26.0),
    food("p03","খাসির মাংস",294.0,0.0,25.0),
    food("p04","হাঁসের মাংস",337.0,0.0,19.0),
    food("p05","ডিম",155.0,0.0,13.0),
    food("p06","ডিমের সাদা অংশ",52.0,0.0,11.0),
    food("p07","ডিমের কুসুম",322.0,0.0,16.0),
    food("p08","গরুর কলিজা",175.0,0.0,26.0),
    food("p09","মুরগির কলিজা",119.0,0.0,17.0),
    food("p10","দুধ",61.0,0.0,3.2),
    food("p11","টক দই",61.0,0.0,3.5),
    food("p12","মিষ্টি দই",120.0,0.0,3.0),
    food("p13","পনির",265.0,0.0,18.0),
    food("p14","মাখন",717.0,0.0,0.9),
    food("p15","ছানা",98.0,0.0,11.0),

    // বাদাম ও বীজ - ১০টি
    food("n01","চিনাবাদাম",567.0,8.5,25.8),
    food("n02","কাঠবাদাম",579.0,12.5,21.2),
    food("n03","কাজুবাদাম",553.0,3.3,18.2),
    food("n04","আখরোট",654.0,6.7,15.2),
    food("n05","পেস্তা",562.0,10.6,20.2),
    food("n06","তিল",573.0,11.8,17.7),
    food("n07","চিয়া বীজ",486.0,34.4,16.5),
    food("n08","তিসি বীজ",534.0,27.3,18.3),
    food("n09","কুমড়ার বীজ",559.0,6.0,30.0),
    food("n10","সূর্যমুখীর বীজ",584.0,8.6,20.8),

    // রান্না ও অন্যান্য খাবার - ৪০টি
    food("c01","খিচুড়ি",120.0,2.0,4.0),
    food("c02","সবজি খিচুড়ি",110.0,3.0,4.0),
    food("c03","ডাল ভাত",140.0,2.0,5.0),
    food("c04","সবজি ভাজি",80.0,3.0,2.0),
    food("c05","আলু ভর্তা",110.0,2.0,2.0),
    food("c06","বেগুন ভর্তা",90.0,3.0,2.0),
    food("c07","ডাল ভর্তা",130.0,7.0,8.0),
    food("c08","শাক ভাজি",70.0,3.0,3.0),
    food("c09","মিশ্র সবজি",65.0,3.0,2.0),
    food("c10","মুরগির স্যুপ",50.0,0.5,5.0),
    food("c11","সবজি স্যুপ",35.0,2.0,1.5),
    food("c12","ডিম সেদ্ধ",155.0,0.0,13.0),
    food("c13","মাছের ঝোল",100.0,0.0,12.0),
    food("c14","মুরগির ঝোল",120.0,0.0,15.0),
    food("c15","গরুর ঝোল",150.0,0.0,14.0),
    food("c16","পায়েস",120.0,0.2,3.0),
    food("c17","সেমাই",160.0,0.5,4.0),
    food("c18","চিড়ার পোলাও",150.0,2.0,4.0),
    food("c19","পান্তা ভাত",100.0,0.5,2.0),
    food("c20","মুড়ি মাখা",120.0,2.0,3.0),
    food("c21","সিঙ্গারা",300.0,2.0,5.0),
    food("c22","সমুচা",260.0,2.0,5.0),
    food("c23","পেঁয়াজু",250.0,4.0,6.0),
    food("c24","বেগুনি",250.0,3.0,4.0),
    food("c25","ফুচকা",200.0,3.0,5.0),
    food("c26","চটপটি",150.0,5.0,6.0),
    food("c27","ঝালমুড়ি",180.0,3.0,4.0),
    food("c28","পিঠা",220.0,1.0,4.0),
    food("c29","ভাপা পিঠা",190.0,1.0,3.0),
    food("c30","চিতই পিঠা",170.0,1.0,3.0),
    food("c31","রসমালাই",200.0,0.0,5.0),
    food("c32","রসগোল্লা",186.0,0.0,4.0),
    food("c33","মিষ্টি",350.0,0.0,6.0),
    food("c34","চানাচুর",500.0,5.0,15.0),
    food("c35","পপকর্ন",387.0,14.5,13.0),
    food("c36","চকলেট",546.0,7.0,4.9),
    food("c37","মধু",304.0,0.0,0.3),
    food("c38","চিনি",387.0,0.0,0.0),
    food("c39","গুড়",383.0,0.0,0.4),
    food("c40","নারকেলের নাড়ু",400.0,5.0,3.0)
)

// একক অনুযায়ী আনুমানিক গ্রাম
private fun unitToGrams(unit: String, amount: Double): Double {
    val multiplier = when (unit) {
        "কাপ" -> 150.0
        "বাটি" -> 180.0
        "পিস" -> 50.0
        "চামচ" -> 15.0
        "গ্লাস" -> 200.0
        else -> 1.0
    }
    return amount * multiplier
}

private fun loadEntries(context: Context, date: String): MutableList<FoodEntry> {
    val prefs = context.getSharedPreferences(
        "piles_care_food",
        Context.MODE_PRIVATE
    )
    val json = prefs.getString("entries_$date", "[]") ?: "[]"
    val result = mutableListOf<FoodEntry>()

    try {
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            val item = array.getJSONObject(i)
            result.add(
                FoodEntry(
                    id = item.getString("id"),
                    name = item.getString("name"),
                    meal = item.getString("meal"),
                    amount = item.getDouble("amount"),
                    unit = item.getString("unit"),
                    grams = item.getDouble("grams"),
                    calories = item.getDouble("calories"),
                    fiber = item.getDouble("fiber"),
                    protein = item.getDouble("protein")
                )
            )
        }
    } catch (_: Exception) {
    }

    return result
}

private fun saveEntries(
    context: Context,
    date: String,
    entries: List<FoodEntry>
) {
    val array = JSONArray()

    entries.forEach { entry ->
        val obj = JSONObject()
        obj.put("id", entry.id)
        obj.put("name", entry.name)
        obj.put("meal", entry.meal)
        obj.put("amount", entry.amount)
        obj.put("unit", entry.unit)
        obj.put("grams", entry.grams)
        obj.put("calories", entry.calories)
        obj.put("fiber", entry.fiber)
        obj.put("protein", entry.protein)
        array.put(obj)
    }

    context.getSharedPreferences(
        "piles_care_food",
        Context.MODE_PRIVATE
    ).edit()
        .putString("entries_$date", array.toString())
        .apply()
}

@Composable
fun PilesCareApp() {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("হোম") }
    val today = LocalDate.now().toString()

    val waterPrefs = remember {
        context.getSharedPreferences(
            "piles_care_water",
            Context.MODE_PRIVATE
        )
    }

    var water by remember {
        mutableIntStateOf(
            if (waterPrefs.getString("water_date", "") == today) {
                waterPrefs.getInt("water_ml", 0)
            } else {
                0
            }
        )
    }

    var waterGoal by remember {
        mutableIntStateOf(waterPrefs.getInt("water_goal", 2000))
    }

    fun updateWater(value: Int) {
        water = value.coerceAtLeast(0)
        waterPrefs.edit()
            .putString("water_date", today)
            .putInt("water_ml", water)
            .apply()
    }

    fun updateGoal(value: Int) {
        waterGoal = value.coerceIn(500, 5000)
        waterPrefs.edit()
            .putInt("water_goal", waterGoal)
            .apply()
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf("হোম", "খাবার", "স্বাস্থ্য", "রিপোর্ট", "সেটিংস")
                    .forEach { tab ->
                        NavigationBarItem(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            icon = {
                                Text(
                                    when (tab) {
                                        "হোম" -> "⌂"
                                        "খাবার" -> "🍎"
                                        "স্বাস্থ্য" -> "♥"
                                        "রিপোর্ট" -> "▤"
                                        else -> "⚙"
                                    }
                                )
                            },
                            label = { Text(tab) }
                        )
                    }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            when (selectedTab) {
                "হোম" -> HomeScreen(
                    water = water,
                    waterGoal = waterGoal,
                    onAddWater = { updateWater(water + it) },
                    onRemoveWater = { updateWater(water - 250) },
                    onGoalChange = { updateGoal(it) },
                    onFoodClick = { selectedTab = "খাবার" }
                )

                "খাবার" -> FoodScreen()

                "স্বাস্থ্য" -> PlaceholderScreen(
                    "স্বাস্থ্য",
                    "এখানে দৈনিক স্বাস্থ্য ও অভ্যাসের তথ্য যোগ করা হবে।"
                )

                "রিপোর্ট" -> PlaceholderScreen(
                    "রিপোর্ট",
                    "এখানে দৈনিক ও সাপ্তাহিক রিপোর্ট যোগ করা হবে।"
                )

                "সেটিংস" -> PlaceholderScreen(
                    "সেটিংস",
                    "অ্যাপের সেটিংস এখানে যোগ করা হবে।"
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    water: Int,
    waterGoal: Int,
    onAddWater: (Int) -> Unit,
    onRemoveWater: () -> Unit,
    onGoalChange: (Int) -> Unit,
    onFoodClick: () -> Unit
) {
    var showGoalDialog by remember { mutableStateOf(false) }

    Text(
        "Piles Care BD",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
    )
    Text("স্বাস্থ্যকর অভ্যাস, প্রতিদিন")

    Spacer(Modifier.height(20.dp))

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("💧 পানি পান", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text("$water ml / $waterGoal ml")

            LinearProgressIndicator(
                progress = {
                    (water.toFloat() / waterGoal).coerceIn(0f, 1f)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onAddWater(250) }) {
                    Text("+২৫০ ml")
                }
                OutlinedButton(onClick = onRemoveWater) {
                    Text("-২৫০ ml")
                }
            }

            TextButton(onClick = { showGoalDialog = true }) {
                Text("দৈনিক লক্ষ্য পরিবর্তন")
            }
        }
    }

    Spacer(Modifier.height(14.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onFoodClick() }
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "🍎 খাবার ও ফাইবার ট্র্যাকার",
                style = MaterialTheme.typography.titleLarge
            )
            Text("আজ কী খেয়েছো, ক্যালরি ও ফাইবার হিসাব রাখো।")
            TextButton(onClick = onFoodClick) {
                Text("খাবারের হিসাব খুলুন →")
            }
        }
    }

    if (showGoalDialog) {
        AlertDialog(
            onDismissRequest = { showGoalDialog = false },
            title = { Text("পানির দৈনিক লক্ষ্য") },
            text = {
                Column {
                    Text("বর্তমান লক্ষ্য: $waterGoal ml")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { onGoalChange(waterGoal - 250) }
                        ) {
                            Text("-২৫০")
                        }
                        OutlinedButton(
                            onClick = { onGoalChange(waterGoal + 250) }
                        ) {
                            Text("+২৫০")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showGoalDialog = false }) {
                    Text("ঠিক আছে")
                }
            }
        )
    }
}

@Composable
fun FoodScreen() {
    val context = LocalContext.current
    val today = LocalDate.now().toString()

    var entries by remember {
        mutableStateOf(loadEntries(context, today))
    }

    var search by remember { mutableStateOf("") }
    var selectedMeal by remember { mutableStateOf("সকালের নাশতা") }
    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
    var amount by remember { mutableStateOf("100") }
    var selectedUnit by remember { mutableStateOf("গ্রাম") }
    var showCustomDialog by remember { mutableStateOf(false) }

    val meals = listOf(
        "সকালের নাশতা",
        "দুপুরের খাবার",
        "রাতের খাবার",
        "স্ন্যাকস"
    )

    val units = listOf("গ্রাম", "কাপ", "বাটি", "পিস", "চামচ", "গ্লাস")

    val calories = entries.sumOf { it.calories }
    val fiber = entries.sumOf { it.fiber }
    val protein = entries.sumOf { it.protein }

    fun addEntry(foodItem: FoodItem, quantity: Double, unit: String) {
        val grams = unitToGrams(unit, quantity)
        val factor = grams / 100.0

        val entry = FoodEntry(
            id = System.currentTimeMillis().toString(),
            name = foodItem.name,
            meal = selectedMeal,
            amount = quantity,
            unit = unit,
            grams = grams,
            calories = foodItem.calories * factor,
            fiber = foodItem.fiber * factor,
            protein = foodItem.protein * factor
        )

        entries = (entries + entry).toMutableList()
        saveEntries(context, today, entries)
        selectedFood = null
        amount = "100"
        selectedUnit = "গ্রাম"
    }

    Text(
        "🍎 খাবার ও ফাইবার",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
    )
    Text("আজকের খাবারের হিসাব রাখো")

    Spacer(Modifier.height(16.dp))

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("আজকের পুষ্টির হিসাব", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(10.dp))
            Text("🔥 ক্যালরি: ${calories.toInt()} kcal")
            Text("🌿 ফাইবার: ${String.format(Locale.US, "%.1f", fiber)} g")
            Text("💪 প্রোটিন: ${String.format(Locale.US, "%.1f", protein)} g")
            Spacer(Modifier.height(8.dp))
            Text(
                "খাবারের পরিমাণ ও রান্নার ধরন অনুযায়ী হিসাব আনুমানিক।",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    Spacer(Modifier.height(16.dp))
    Text("কোন বেলার খাবার?", fontWeight = FontWeight.Bold)

    meals.forEach { meal ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedMeal = meal }
                .padding(vertical = 3.dp)
        ) {
            RadioButton(
                selected = selectedMeal == meal,
                onClick = { selectedMeal = meal }
            )
            Text(meal, modifier = Modifier.padding(top = 12.dp))
        }
    }

    Spacer(Modifier.height(10.dp))
    Text("খাবার খুঁজুন", fontWeight = FontWeight.Bold)

    OutlinedTextField(
        value = search,
        onValueChange = { search = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("যেমন: ভাত, ডাল, মাছ, পেয়ারা") },
        singleLine = true
    )

    Spacer(Modifier.height(10.dp))

    val filteredFoods = foodDatabase.filter {
        it.name.contains(search.trim(), ignoreCase = true)
    }

    Text(
        "খাবারের তালিকা (${filteredFoods.size}টি)",
        fontWeight = FontWeight.Bold
    )

    filteredFoods.forEach { foodItem ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable {
                    selectedFood = foodItem
                    amount = "100"
                    selectedUnit = "গ্রাম"
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(foodItem.name, fontWeight = FontWeight.Bold)
                    Text(
                        "প্রতি ১০০ গ্রাম: ${foodItem.calories.toInt()} kcal · ফাইবার ${foodItem.fiber} g",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text("＋", style = MaterialTheme.typography.titleLarge)
            }
        }
    }

    Spacer(Modifier.height(16.dp))
    Text("আজকের খাবারের তালিকা", style = MaterialTheme.typography.titleLarge)

    if (entries.isEmpty()) {
        Text("এখনো কোনো খাবার যোগ করা হয়নি।")
    } else {
        entries.forEach { entry ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(entry.name, fontWeight = FontWeight.Bold)
                    Text("${entry.meal} · ${entry.amount} ${entry.unit}")
                    Text(
                        "${entry.calories.toInt()} kcal · ফাইবার ${
                            String.format(Locale.US, "%.1f", entry.fiber)
                        } g · প্রোটিন ${
                            String.format(Locale.US, "%.1f", entry.protein)
                        } g"
                    )

                    TextButton(
                        onClick = {
                            entries = entries.filterNot {
                                it.id == entry.id
                            }.toMutableList()
                            saveEntries(context, today, entries)
                        }
                    ) {
                        Text("মুছে ফেলুন")
                    }
                }
            }
        }
    }

    Spacer(Modifier.height(12.dp))

    OutlinedButton(
        onClick = { showCustomDialog = true },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("＋ নিজের খাবার যোগ করুন")
    }

    if (selectedFood != null) {
        val currentFood = selectedFood!!

        AlertDialog(
            onDismissRequest = { selectedFood = null },
            title = { Text(currentFood.name) },
            text = {
                Column {
                    Text("পরিমাণ লিখুন")

                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        singleLine = true,
                        placeholder = { Text("যেমন: 100") }
                    )

                    Spacer(Modifier.height(8.dp))
                    Text("একক নির্বাচন করুন")

                    units.forEach { unit ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedUnit = unit }
                        ) {
                            RadioButton(
                                selected = selectedUnit == unit,
                                onClick = { selectedUnit = unit }
                            )
                            Text(unit, modifier = Modifier.padding(top = 12.dp))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val quantity = amount.toDoubleOrNull()
                        if (quantity != null && quantity > 0) {
                            addEntry(currentFood, quantity, selectedUnit)
                        }
                    }
                ) {
                    Text("যোগ করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedFood = null }) {
                    Text("বাতিল")
                }
            }
        )
    }

    if (showCustomDialog) {
        CustomFoodDialog(
            onDismiss = { showCustomDialog = false },
            onAdd = { name, kcal, fiberValue, proteinValue ->
                val customFood = FoodItem(
                    id = "custom_${System.currentTimeMillis()}",
                    name = name,
                    calories = kcal,
                    fiber = fiberValue,
                    protein = proteinValue
                )

                addEntry(customFood, 100.0, "গ্রাম")
                showCustomDialog = false
            }
        )
    }
}

@Composable
fun CustomFoodDialog(
    onDismiss: () -> Unit,
    onAdd: (String, Double, Double, Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var kcal by remember { mutableStateOf("") }
    var fiber by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("নিজের খাবার যোগ করুন") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("খাবারের নাম") }
                )

                OutlinedTextField(
                    value = kcal,
                    onValueChange = { kcal = it },
                    label = { Text("ক্যালরি / ১০০ গ্রাম") }
                )

                OutlinedTextField(
                    value = fiber,
                    onValueChange = { fiber = it },
                    label = { Text("ফাইবার / ১০০ গ্রাম") }
                )

                OutlinedTextField(
                    value = protein,
                    onValueChange = { protein = it },
                    label = { Text("প্রোটিন / ১০০ গ্রাম") }
                )

                Text(
                    "পুষ্টির মান নিজে যাচাই করে লিখবে।",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val c = kcal.toDoubleOrNull()
                    val f = fiber.toDoubleOrNull()
                    val p = protein.toDoubleOrNull()

                    if (
                        name.isNotBlank() &&
                        c != null && f != null && p != null &&
                        c >= 0 && f >= 0 && p >= 0
                    ) {
                        onAdd(name.trim(), c, f, p)
                    }
                }
            ) {
                Text("যোগ করুন")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("বাতিল")
            }
        }
    )
}

@Composable
fun PlaceholderScreen(title: String, message: String) {
    Text(
        title,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(12.dp))
    Text(message)
}
