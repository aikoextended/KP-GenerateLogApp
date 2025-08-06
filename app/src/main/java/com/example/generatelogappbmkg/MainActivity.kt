package com.example.generatelogappbmkg

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.util.*
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*



class MainActivity : ComponentActivity() {
    @Composable
    fun Color(hex: String): Color = Color(android.graphics.Color.parseColor("#$hex"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val logItems = remember { mutableStateListOf(LogEntry()) }

            Header()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // space antar header
                Spacer(modifier = Modifier.height(72.dp))

                LinkInput()

                Spacer(modifier = Modifier.height(8.dp))

                logItems.forEachIndexed { index, logEntry ->
                    LogCard(index, logEntry, onDelete = {
                        logItems.removeAt(index)
                    })
                    Spacer(modifier = Modifier.height(4.dp))
                }

                AddLogButton {
                    logItems.add(LogEntry())
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(Color("0133CC")),
                    onClick = {
                        Toast.makeText(this@MainActivity, "Data dikirim!", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text(text = "Kirim", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }

    // HEADER
    @Composable
    fun Header() {
        val density = LocalDensity.current

        Column(
            modifier = Modifier
                .fillMaxWidth() // Full kiri-kanan
                .background(Color.White)
                .drawBehind {
                    // Shadow hitam semi transparan di bawah
                    val shadowHeight = with(density) { 1.dp.toPx() }
                    drawRect(
                        color = Color.Black.copy(alpha = 0.10f),
                        topLeft = Offset(0f, size.height - shadowHeight),
                        size = Size(size.width, shadowHeight)
                    )
                }
                .padding(top = 12.dp, bottom = 12.dp) // Tidak pakai padding kiri-kanan!
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp) // Khusus isi, diberi padding kiri
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_bmkg),
                    contentDescription = "Logo",
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        "BADAN METEOROLOGI,",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        "KLIMATOLOGI, DAN GEOFISIKA",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }


    // LINK
    @Composable
    fun LinkInput() {
        var link by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF9F9F9))
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "LINK",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = link,
                onValueChange = { link = it },
                placeholder = { Text("masukkan link..", color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_link),
                        contentDescription = "Link Icon",
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
        }
    }

    // LOG CARD
    @Composable
    fun LogCard(
        logNumber: Int = 1,
        logEntry: LogEntry,
        onDelete: () -> Unit = {}
    ) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()

        var selectedDate by remember { mutableStateOf(logEntry.date) }
        var selectedTime by remember { mutableStateOf(logEntry.time) }
        var selectedShift by remember { mutableStateOf(logEntry.shift) }
        var selectedModel by remember { mutableStateOf(logEntry.modelKerja) }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color(0xFFA1A7B3)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "Log $logNumber",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                // DATE
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                selectedDate = String.format("%02d/%02d/%d", day, month + 1, year)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = null,
                        tint = Color(0xFFA1A7B3),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (selectedDate.isNotEmpty()) selectedDate else "DD/MM/YY",
                        fontSize = 14.sp,
                        color = Color(0xFFA1A7B3)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // TIME
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                val isAM = hour < 12
                                val formattedHour = if (hour % 12 == 0) 12 else hour % 12
                                selectedTime = String.format("%02d:%02d %s", formattedHour, minute, if (isAM) "AM" else "PM")
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false
                        ).show()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_time),
                        contentDescription = null,
                        tint = Color(0xFFA1A7B3),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (selectedTime.isNotEmpty()) selectedTime else "00:00 AM",
                        fontSize = 14.sp,
                        color = Color(0xFFA1A7B3)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                DropdownInput(
                    label = "Shift",
                    options = listOf("Shift Pagi", "Shift Siang", "Shift Malam"),
                    selected = selectedShift,
                    onSelectedChange = { selectedShift = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                DropdownInput(
                    label = "Model Kerja",
                    options = listOf("WFO", "WFH", "Dinas Luar"),
                    selected = selectedModel,
                    onSelectedChange = { selectedModel = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDelete,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFEBEB),
                            contentColor = Color(0xFFF46352)
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = null,
                            tint = Color(0xFFF46352),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Delete", fontSize = 14.sp)
                    }
                }
            }
        }
    }

    private fun showDeleteDialog(context: android.content.Context, onConfirm: () -> Unit) {
        android.app.AlertDialog.Builder(context)
            .setMessage("Yakin menghapus log?")
            .setPositiveButton("Yes") { _, _ -> onConfirm() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    @Composable
    fun AddLogButton(onClick: () -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.ic_add_circle),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onClick() }
                )
                Text("Add Log", color = Color(0xFF0133CC)) // tetap pakai warna biru untuk teks
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

    data class LogEntry(
        var date: String = "",
        var time: String = "",
        var shift: String = "",
        var modelKerja: String = ""
    )
}
