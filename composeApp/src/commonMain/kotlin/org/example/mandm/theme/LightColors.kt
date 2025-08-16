// MilkSellerTheme.kt
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.example.mandm.theme.AppTypography

// Light Theme Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF103D27),
    onPrimary = Color.White,
    secondary = Color(0xFFFF9800),
    onSecondary = Color.Black,
    background = Color(0xFFF1FDF2),
    onBackground = Color(0xFF212121),
    surface = Color.White,
    onSurface = Color.Black,
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF004938),     // Consistent dark green
    onPrimary = Color.White,
    secondary = Color(0xFFFF9800),   // Same orange
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
)

@Composable
fun MilkSellerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // works in Android/Desktop
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = AppTypography(), // You can customize font here later
        content = content
    )
}
