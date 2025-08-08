package com.example.generatelogappbmkg

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

class MainActivity : ComponentActivity() {

    @Composable
    fun Color(hex: String): Color = Color(android.graphics.Color.parseColor("#$hex"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var link by remember { mutableStateOf("") }
            var date by remember { mutableStateOf("") }
            var time by remember { mutableStateOf("") }
            var shift by remember { mutableStateOf("") }
            var modelKerja by remember { mutableStateOf("") }
            val generatedLogs = remember { mutableStateListOf<String>() }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Header()

                Column(modifier = Modifier.padding(16.dp)) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("LINK", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = link,
                        onValueChange = { link = it },
                        placeholder = { Text("masukkan link..", color = Color.Gray) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_link),
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.LightGray,
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Log Form Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color("A1A7B3"))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Log", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(16.dp))

                            val calendar = Calendar.getInstance()
                            val context = LocalContext.current

                            // === Row untuk Tanggal & Waktu ===
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Date
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(1.dp, Color("A1A7B3"), RoundedCornerShape(8.dp))
                                        .clickable {
                                            DatePickerDialog(
                                                context,
                                                { _, y, m, d ->
                                                    date = String.format("%04d-%02d-%02d", y, m + 1, d)
                                                },
                                                calendar.get(Calendar.YEAR),
                                                calendar.get(Calendar.MONTH),
                                                calendar.get(Calendar.DAY_OF_MONTH)
                                            ).show()
                                        }
                                        .padding(12.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_calendar),
                                        contentDescription = null,
                                        tint = Color("A1A7B3"),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (date.isNotEmpty()) date else "DD/MM/YY",
                                        color = Color("A1A7B3")
                                    )
                                }

                                // Time
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(1.dp, Color("A1A7B3"), RoundedCornerShape(8.dp))
                                        .clickable {
                                            TimePickerDialog(
                                                context,
                                                { _, hour, minute ->
                                                    val ampm = if (hour < 12) "AM" else "PM"
                                                    val h = if (hour % 12 == 0) 12 else hour % 12
                                                    time = String.format("%02d:%02d %s", h, minute, ampm)
                                                },
                                                calendar.get(Calendar.HOUR_OF_DAY),
                                                calendar.get(Calendar.MINUTE),
                                                false
                                            ).show()
                                        }
                                        .padding(12.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_time),
                                        contentDescription = null,
                                        tint = Color("A1A7B3"),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (time.isNotEmpty()) time else "0.00 AM",
                                        color = Color("A1A7B3")
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // === Row untuk Shift & Model Kerja ===
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    DropdownInput("Shift", listOf("P", "S", "M"), shift) { shift = it }
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    DropdownInput("Model Kerja", listOf("WFO", "WFH", "DL"), modelKerja) { modelKerja = it }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                modifier = Modifier.align(Alignment.End),
                                onClick = {
                                    if (date.isNotEmpty() && time.isNotEmpty() && shift.isNotEmpty() && modelKerja.isNotEmpty()) {
                                        generatedLogs.add("$date, $time, $shift, $modelKerja")
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color("0133CC"))
                            ) {
                                Text("Generate", color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Generated", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, Color("A1A7B3"))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            generatedLogs.forEachIndexed { index, item ->
                                Text("${index + 1}. $item", fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color("0133CC")),
                        onClick = {
                            Toast.makeText(this@MainActivity, "Data dikirim!", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text("Kirim", color = Color.White, fontSize = 18.sp)
                    }
                }
            }
        }
    }

    @Composable
    fun Header() {
        val density = LocalDensity.current

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .drawBehind {
                    val shadowHeight = with(density) { 1.dp.toPx() }
                    drawRect(
                        color = Color.Black.copy(alpha = 0.1f),
                        topLeft = Offset(0f, size.height - shadowHeight),
                        size = Size(size.width, shadowHeight)
                    )
                }
                .padding(top = 12.dp, bottom = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_bmkg),
                    contentDescription = "Logo",
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("BADAN METEOROLOGI,", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("KLIMATOLOGI, DAN GEOFISIKA", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    @Composable
    fun DropdownInput(
        label: String,
        options: List<String>,
        selected: String,
        onSelectedChange: (String) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        Column {
            Text(text = label, fontSize = 14.sp, color = Color.Black)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color("A1A7B3"), RoundedCornerShape(8.dp))
                    .clickable { expanded = true }
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (selected.isNotEmpty()) selected else "Select option",
                        color = if (selected.isNotEmpty()) Color.Black else Color("A1A7B3")
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_dropdown),
                        contentDescription = null,
                        tint = Color("A1A7B3")
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onSelectedChange(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
