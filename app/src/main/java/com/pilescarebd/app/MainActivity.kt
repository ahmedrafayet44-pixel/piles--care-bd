package com.pilescarebd.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.collect
import java.time.LocalDate

private val BrandGreen = Color(0xFF168B68)
private val DeepGreen = Color(0xFF0C604A)
private val Mint = Color(0xFFEAF6F0)
private val PageBg = Color(0xFFF5F8F6)
private val Ink = Color(0xFF20332B)
private val Muted = Color(0xFF718078)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = BrandGreen,
                    onPrimary = Color.White,
                    background = PageBg,
                    surface = Color.White,
                    onSurface = Ink
                )
            ) {
                PilesCareApp()
            }
        }
    }
}

@Composable
fun PilesCareApp() {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("piles_care_data", 0)
    }

    val today = LocalDate.now().toString()
    val savedDate = prefs.getString("water_date", "")

    var water by remember {
        mutableIntStateOf(
            if (savedDate == today) prefs.getInt("water_ml", 0)
            else 0
        )
    }

    var goal by remember {
        mutableIntStateOf(prefs.getInt("water_goal", 2000))
    }

    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(water, goal) {
        prefs.edit()
            .putString("water_date", today)
            .putInt("water_ml", water)
            .putInt("water_goal", goal)
            .apply()
    }

    val progress = (water.toFloat() / goal).coerceIn(0f, 1f)

    Scaffold(
        containerColor = PageBg,
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                val tabs = listOf(
                    Triple("⌂", "হোম", 0),
                    Triple("🍎", "খাবার", 1),
                    Triple("♡", "স্বাস্থ্য", 2),
                    Triple("▥", "রিপোর্ট", 3),
                    Triple("⚙", "সেটিংস", 4)
                )

                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab.third,
                        onClick = { selectedTab = tab.third },
                        icon = {
                            Text(
                                tab.first,
                                fontSize = 21.sp,
                                color = if (selectedTab == tab.third)
                                    BrandGreen else Muted
                            )
                        },
                        label = {
                            Text(tab.second, fontSize = 10.sp)
                        },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrandGreen,
                            selectedTextColor = BrandGreen,
                            indicatorColor = Mint,
                            unselectedIconColor = Muted,
                            unselectedTextColor = Muted
                        )
                    )
                }
            }
        }
    ) { innerPadding ->

        if (selectedTab == 0) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    start = 18.dp,
                    end = 18.dp,
                    top = 18.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(17.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("শুভ দিন! 👋", color = Muted)
                            Text(
                                "Piles Care BD",
                                color = DeepGreen,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "প্রতিদিন একটু একটু যত্ন",
                                color = Muted,
                                fontSize = 12.sp
                            )
                        }

                        Box(
                            Modifier
                                .size(48.dp)
                                .background(Mint, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("💚", fontSize = 25.sp)
                        }
                    }
                }

                item {
                    WaterCard(
                        water = water,
                        goal = goal,
                        progress = progress,
                        onAdd = { amount ->
                            water += amount
                        },
                        onRemove = {
                            water = (water - 250).coerceAtLeast(0)
                        },
                        onGoalChange = { goal = it }
                    )
                }

                item {
                    SectionHeading("আজকের যত্ন", "দৈনিক রেকর্ড")
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SummaryTile(
                            Modifier.weight(1f),
                            "🍎",
                            "খাবার",
                            "এখনো যোগ হয়নি",
                            Color(0xFFFFF3E8)
                        )
                        SummaryTile(
                            Modifier.weight(1f),
                            "🌿",
                            "ফাইবার",
                            "রেকর্ড নেই",
                            Mint
                        )
                    }
                }

                item {
                    SectionHeading("দৈনিক যত্ন", "নিজের রেকর্ড রাখুন")
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        ActionRow(
                            "🍽️",
                            "খাবারের ডায়েরি",
                            "আজ কী খেয়েছো লিখে রাখো"
                        )
                        ActionRow(
                            "🩺",
                            "পাইলস ও পায়খানার রেকর্ড",
                            "জ্বালা, ফোলা ও পায়খানার অবস্থা"
                        )
                        ActionRow(
                            "💊",
                            "ওষুধ ও রিমাইন্ডার",
                            "ওষুধের সময়সূচি সাজাও"
                        )
                    }
                }

                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                "আজকের ছোট লক্ষ্য",
                                color = Ink,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            GoalLine("💧", "পানি পান", "পানির পরিমাণ নোট করো")
                            HorizontalDivider(color = Color(0xFFEDF1EE))
                            GoalLine("🥗", "আঁশযুক্ত খাবার", "শাকসবজি ও ফলের হিসাব রাখো")
                            HorizontalDivider(color = Color(0xFFEDF1EE))
                            GoalLine("📝", "লক্ষণ লিখে রাখো", "প্রতিদিনের পরিবর্তন লক্ষ্য করো")
                        }
                    }
                }

                item {
                    Text(
                        "তোমার পানির হিসাব এই ফোনেই সংরক্ষিত থাকে।",
                        color = Muted,
                        fontSize = 11.sp
                    )
                }
            }
        } else {
            PlaceholderPage(
                title = listOf(
                    "হোম",
                    "খাবারের ডায়েরি",
                    "স্বাস্থ্য রেকর্ড",
                    "রিপোর্ট",
                    "সেটিংস"
                )[selectedTab],
                onHome = { selectedTab = 0 }
            )
        }
    }
}

@Composable
private fun WaterCard(
    water: Int,
    goal: Int,
    progress: Float,
    onAdd: (Int) -> Unit,
    onRemove: () -> Unit,
    onGoalChange: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEAF5FF)
        )
    ) {
        Column(
            Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "💧 পানির হিসাব",
                        color = Color(0xFF245B7A),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    Text(
                        "আজ কতটুকু পানি পান করেছো?",
                        color = Color(0xFF668397),
                        fontSize = 11.sp
                    )
                }
                Text("WATER", color = Color(0xFF5793B9), fontSize = 10.sp)
            }

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "$water",
                    color = Color(0xFF176C9C),
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(" ml", color = Color(0xFF397FA6))
                Spacer(Modifier.weight(1f))
                Text("লক্ষ্য $goal ml", color = Color(0xFF527C94))
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                color = Color(0xFF2796D2),
                trackColor = Color(0xFFD2E8F5)
            )

            Text(
                "${(progress * 100).toInt()}% সম্পন্ন",
                color = Color(0xFF527C94),
                fontSize = 12.sp
            )

            Text(
                if (water >= goal) "🎉 আজকের নির্ধারিত লক্ষ্য পূরণ হয়েছে!"
                else "আরও ${goal - water} ml বাকি",
                color = Color(0xFF245B7A),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )

            Text(
                "পানি যোগ করুন",
                color = Color(0xFF245B7A),
                fontWeight = FontWeight.SemiBold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                WaterButton("＋ ২৫০", Modifier.weight(1f)) {
                    onAdd(250)
                }
                WaterButton("＋ ৫০০", Modifier.weight(1f)) {
                    onAdd(500)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                WaterButton("＋ ১০০০", Modifier.weight(1f)) {
                    onAdd(1000)
                }

                OutlinedButton(
                    onClick = onRemove,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("− ২৫০ ml")
                }
            }

            HorizontalDivider(color = Color(0xFFD2E8F5))

            Text(
                "দৈনিক পানির লক্ষ্য পরিবর্তন",
                color = Color(0xFF245B7A),
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        onGoalChange((goal - 250).coerceAtLeast(500))
                    }
                ) {
                    Text("−")
                }

                Text(
                    "$goal ml",
                    color = Color(0xFF176C9C),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                OutlinedButton(
                    onClick = {
                        onGoalChange((goal + 250).coerceAtMost(5000))
                    }
                ) {
                    Text("+")
                }
            }

            Text(
                "লক্ষ্যটি নিজের প্রয়োজন অনুযায়ী ঠিক করো।",
                color = Color(0xFF668397),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun WaterButton(
    label: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 5.dp, vertical = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1687C3)
        )
    ) {
        Text(label, fontSize = 12.sp)
    }
}

@Composable
private fun SectionHeading(title: String, subtitle: String) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            color = Ink,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Text(subtitle, color = Muted, fontSize = 10.sp)
    }
}

@Composable
private fun SummaryTile(
    modifier: Modifier,
    emoji: String,
    title: String,
    value: String,
    tint: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Box(
                Modifier
                    .size(36.dp)
                    .background(tint, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 19.sp)
            }
            Text(
                title,
                color = Ink,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(value, color = Muted, fontSize = 10.sp)
        }
    }
}

@Composable
private fun ActionRow(
    emoji: String,
    title: String,
    description: String
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(43.dp)
                    .background(Mint, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 22.sp)
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    color = Ink,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(description, color = Muted, fontSize = 10.sp)
            }

            Text("＋", color = BrandGreen, fontSize = 23.sp)
        }
    }
}

@Composable
private fun GoalLine(
    emoji: String,
    title: String,
    description: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(emoji, fontSize = 22.sp)
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                title,
                color = Ink,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(description, color = Muted, fontSize = 10.sp)
        }
    }
}

@Composable
private fun PlaceholderPage(
    title: String,
    onHome: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🌱", fontSize = 48.sp)
        Spacer(Modifier.height(12.dp))
        Text(
            title,
            color = DeepGreen,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "এই বিভাগটি পরবর্তী আপডেটে তৈরি করা হবে।",
            color = Muted,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(18.dp))
        Button(onClick = onHome) {
            Text("হোমে ফিরে যাও")
        }
    }
}
