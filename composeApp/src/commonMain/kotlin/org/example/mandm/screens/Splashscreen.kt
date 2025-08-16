package org.example.mandm.screens

//import mandm.composeapp.generated.resources.milk_carton_icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToMain:()-> Unit){
    LaunchedEffect("key") {
        delay(3000)
        onNavigateToMain.invoke()
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center){
//        Image(contentDescription = "splash logo", painter = painterResource(Res.drawable.milk_carton_icon), modifier = Modifier.size(100.dp))

    }
}

