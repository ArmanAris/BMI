package com.aris.bmi

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.*
import com.aris.bmi.navhost.Page
import com.aris.bmi.navhost.SetupNavGraph
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var start by remember { mutableStateOf(false) }
            Welcome()
            lifecycleScope.launch {
                delay(2000)
                start = true
            }

            if (start) {
                navController = rememberNavController()
                SetupNavGraph(navController = navController,this)
            }

        }
    }
}

@Composable
fun Welcome() {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.weight))
        LottieAnimation(
            composition,
            isPlaying = true,
            iterations = LottieConstants.IterateForever,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
            speed = 1f,
        )

    }
}

@Composable
fun View(navController: NavController,context: Context) {

    val maleOrFemale = remember { mutableStateOf<Boolean?>(null) }
    val sliderPosition = remember { mutableStateOf(1.5f) }
    val sliderPositionToInt = (sliderPosition.value * 100).roundToInt()
    val wight = remember { mutableStateOf(55u) }
    val age = remember { mutableStateOf(22u) }
    val bmi =
        wight.value.toFloat() / ((sliderPositionToInt.toFloat() / 100) * (sliderPositionToInt.toFloat() / 100))


    if (wight.value > 400u) wight.value = 400u
    if (age.value > 200u) age.value = 200u

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {

        First(Modifier
            .fillMaxSize()
            .weight(0.3f)
            .padding(20.dp), maleOrFemale)

        Second(Modifier
            .fillMaxSize()
            .weight(0.3f)
            .padding(horizontal = 30.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.Gray)
            .padding(20.dp), sliderPosition)

        Third(Modifier
            .fillMaxSize()
            .weight(0.3f)
            .padding(20.dp), wight, age)

        Button(onClick = {
            if (maleOrFemale.value != null) {
                navController.navigate(route = Page.Second.route + "/$bmi")

            } else {
                Toast.makeText( context,"خطلا", Toast.LENGTH_SHORT).show()
                openDialogState.value = true
            }
        },
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
                .padding(vertical = 12.dp, horizontal = 18.dp)
                .clip(RoundedCornerShape(19.dp)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Magenta,
                contentColor = Color.White)
        ) {
            Text(text = "Calculate Your BMI",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.raleway)),
                fontWeight = FontWeight.Bold)
        }
    }
    AlertDialog()
}

@Composable
fun First(modifier: Modifier, maleOrFemale: MutableState<Boolean?>) {
    Row(modifier = modifier) {
        var maleColor by remember { mutableStateOf(Color.Gray) }
        var femaleColor by remember { mutableStateOf(Color.Gray) }

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(maleColor)
                .fillMaxSize()
                .weight(0.5f)
                .clickable {
                    maleColor = Color.LightGray
                    femaleColor = Color.Gray
                    maleOrFemale.value = true
                }) {
            Image(painter = painterResource(id = R.drawable.male),
                contentDescription = "male",
                modifier = Modifier
                    .fillMaxSize(0.6f)
                    .padding(5.dp))
            Text(text = "male",
                color = Color.White, fontFamily = FontFamily(Font(R.font.playlist)))
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center, modifier = Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(femaleColor)
                .fillMaxSize()
                .weight(0.5f)
                .clickable {
                    femaleColor = Color.LightGray
                    maleColor = Color.Gray
                    maleOrFemale.value = false
                }) {
            Image(painter = painterResource(id = R.drawable.female),
                contentDescription = "female",
                modifier = Modifier
                    .fillMaxSize(0.6f)
                    .padding(5.dp))
            Text(text = "Female",
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.playlist)))
        }

    }
}

@Composable
fun Second(modifier: Modifier, sliderPosition: MutableState<Float>) {


    Column(modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Height",
            color = Color.White,
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.playlist)))
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Text(text = (sliderPosition.value * 100).roundToInt().toString(),
                color = Color.White,
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold, fontFamily = FontFamily(Font(R.font.raleway)))
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "cm",
                color = Color.White,
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.raleway)))
        }

        Slider(value = sliderPosition.value,
            onValueChange = { sliderPosition.value = it },
            valueRange = 0f..3f,
            colors = SliderDefaults.colors(
                thumbColor = Color.Magenta,
                activeTrackColor = Color.Magenta
            ))

    }
}

@Composable
fun Third(modifier: Modifier, wight: MutableState<UInt>, age: MutableState<UInt>) {
    Row(modifier = modifier) {


        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.Gray)
                .fillMaxSize()
                .weight(0.5f)
                .padding(10.dp)) {
            Text(text = "Wight",
                color = Color.White,
                modifier = Modifier.weight(0.4f),
                fontSize = 30.sp, fontFamily = FontFamily(Font(R.font.playlist)))
            Text(text = wight.value.toString(),
                color = Color.White,
                modifier = Modifier.weight(0.4f),
                fontSize = 30.sp, fontFamily = FontFamily(Font(R.font.raleway)))
            Row(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f),
                horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = {
                    wight.value--
                }) {
                    Icon(painter = painterResource(id = R.drawable.minus),
                        contentDescription = "minus", tint = Color.White)
                }
                IconButton(onClick = { wight.value++ }) {
                    Icon(painter = painterResource(id = R.drawable.add),
                        contentDescription = "add",
                        tint = Color.White)
                }
            }

        }

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(10.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.Gray)
                .fillMaxSize()
                .weight(0.5f)
                .padding(10.dp)) {
            Text(text = "Age",
                color = Color.White,
                modifier = Modifier.weight(0.4f),
                fontSize = 30.sp, fontFamily = FontFamily(Font(R.font.playlist)))
            Text(text = age.value.toString(),
                color = Color.White,
                modifier = Modifier.weight(0.4f),
                fontSize = 30.sp, fontFamily = FontFamily(Font(R.font.raleway)))
            Row(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f),
                horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = {
                    age.value--
                }) {
                    Icon(painter = painterResource(id = R.drawable.minus),
                        contentDescription = "minus", tint = Color.White)
                }
                IconButton(onClick = { age.value++ }) {
                    Icon(painter = painterResource(id = R.drawable.add),
                        contentDescription = "add",
                        tint = Color.White)
                }
            }

        }

    }
}

@Composable
fun Women(navController: NavController, bmi: Float) {
    var text by remember { mutableStateOf("Normal") }
    var color by remember { mutableStateOf(Color.Green) }
    var colorText by remember { mutableStateOf(Color.Black) }
    var image by remember { mutableStateOf(R.drawable.ok) }

    if (bmi < 18.5) {
        text = "Underweight"
        color = Color.Yellow
        image = R.drawable.warning
    } else if (bmi < 24.9) {
        text = "Normal weight"
        color = Color.Green
        image = R.drawable.ok
    } else {
        text = "Overweight"
        color = Color.Red
        image = R.drawable.cross
        colorText = Color.White
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF1E1D1D)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Image(painter = painterResource(id = image),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .fillMaxHeight(0.4f)
                .weight(0.3f))

        Row(modifier = Modifier
            .padding(horizontal = 50.dp, vertical = 20.dp)
            .fillMaxWidth()
            .weight(0.6f),
            horizontalArrangement = Arrangement.Center) {
            Column(modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .background(color)
                .fillMaxWidth()
                .fillMaxSize(0.5f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = text,
                    color = colorText,
                    maxLines = 1,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(20.dp), fontWeight = FontWeight.Bold)
            }

        }

        Button(onClick = {
            navController.navigate(route = Page.Home.route) {
                popUpTo(Page.Home.route) { inclusive = true }
            }

        },
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
                .padding(vertical = 12.dp, horizontal = 18.dp)
                .clip(RoundedCornerShape(19.dp)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Magenta,
                contentColor = Color.White)
        ) {
            Text(text = "Back to Calculate BMI",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.raleway)),
                fontWeight = FontWeight.Bold)
        }

    }

}

val openDialogState = mutableStateOf(false)

@Composable
fun AlertDialog() {
    val openDialog by remember { openDialogState }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialogState.value = false },
            title = { Text(text = "جنسیت را مشخص کنید!") },
            confirmButton = {
                Button(onClick = {
                    openDialogState.value = false
                }) {
                    Text(text = "Ok")
                }
            }
        )
    }

}
