package com.pilescarebd.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green = Color(0xFF168B68)
private val Background = Color(0xFFF4F7F5)

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

@Composable
fun PilesCareApp() {
    var water by remember { mutableIntStateOf(0) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Background
    ) {
        LazyColumn(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Piles Care BD",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Green
                        )
                        Text("তোমার প্রতিদিনের স্বাস্থ্যসঙ্গী")
                    }
                    Text("💚", fontSize = 35.sp)
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE2F3FF)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text("💧 আজকের পানি", fontSize = 20.sp)
                        Text(
                            "$water ml",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1679BB)
                        )
                        Text("দৈনিক লক্ষ্যমাত্রা: ২০০০ ml")
                        Spacer(Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = {
                                (water / 2000f).coerceIn(0f, 1f)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFF1689D5)
                        )
                        Spacer(Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = {
                                water += 250
                            }) {
                                Text("+ ২৫০ ml")
                            }
                            OutlinedButton(onClick = {
                                water = (water - 250).coerceAtLeast(0)
                            }) {
                                Text("- ২৫০ ml")
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    "আজকের স্বাস্থ্য",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                DashboardCard("🍎", "খাবারের ডায়েরি",
                    "আজ কী কী খেয়েছো যোগ করো")
            }

            item {
                DashboardCard("🥬", "পুষ্টির হিসাব",
                    "ভিটামিন, মিনারেল ও ফাইবার")
            }

            item {
                DashboardCard("🩺", "পাইলস ট্র্যাকার",
                    "জ্বালা, গুটি ও পায়খানার রেকর্ড")
            }

            item {
                DashboardCard("💊", "ওষুধের রিমাইন্ডার",
                    "ওষুধের সময় ও দৈনিক হিসাব")
            }

            item {
                DashboardCard("📊", "স্বাস্থ্য রিপোর্ট",
                    "৭ দিন ও ৩০ দিনের অগ্রগতি")
            }

            item {
                DashboardCard("⏰", "রিমাইন্ডার",
                    "পানি, খাবার ও অন্যান্য সময়সূচি")
            }

            item {
                Text(
                    "Piles Care BD • Personal Health App",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun DashboardCard(
    emoji: String,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(emoji, fontSize = 30.sp)
            Spacer(Modifier.width(14.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(description, color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}
