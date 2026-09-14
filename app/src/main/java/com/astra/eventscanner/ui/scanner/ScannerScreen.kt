package com.astra.eventscanner.ui.scanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.astra.eventscanner.ui.components.ScannerFrame
import com.astra.eventscanner.ui.theme.Black
import com.astra.eventscanner.ui.theme.ElectricLime
import java.util.concurrent.Executors
import androidx.compose.ui.tooling.preview.Preview as ComposePreview
@Composable
fun ScannerScreen(
    selectedEventId: Int,
    eventName: String,
    viewModel: ScannerViewModel,
    onResultNavigate: () -> Unit,
    onStatsClick: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scanResult by viewModel.scanResult.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission = granted }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    if (scanResult != null) {
        onResultNavigate()
    }

    Box(modifier = Modifier.fillMaxSize().background(Black)) {
        if (hasCameraPermission) {
            CameraPreview(
                onQrDetected = { token -> if (isScanning) viewModel.onQrDetected(token, selectedEventId) }
            )
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Camera Permission Required", color = Color.White)
            }
        }

        // Scanner Frame Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
            contentAlignment = Alignment.Center
        ) {
            ScannerFrame()
        }

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Black.copy(alpha = 0.6f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = eventName.uppercase(),
                    color = ElectricLime,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
                Text(
                    text = "SCANNING TICKET...",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }

            Box(
                modifier = Modifier
                    .background(ElectricLime)
                    .border(2.dp, Black)
                    .clickable(onClick = onStatsClick)
                    .padding(8.dp)
            ) {
                Text(text = "STATS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Black)
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ElectricLime)
            }
        }
    }
}

@Composable
fun CameraPreview(onQrDetected: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val executor = Executors.newSingleThreadExecutor()
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(executor, QrCodeAnalyzer(onQrDetected))
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}

@ComposePreview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ScannerScreenPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
        // Fake camera background
        Box(
            modifier = Modifier.fillMaxSize()
        )

        // Scanner frame
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
            contentAlignment = Alignment.Center
        ) {
            ScannerFrame()
        }

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Black.copy(alpha = 0.6f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "SAMPLE EVENT",
                    color = ElectricLime,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )

                Text(
                    text = "SCANNING TICKET...",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }

            Box(
                modifier = Modifier
                    .background(ElectricLime)
                    .border(2.dp, Black)
                    .padding(8.dp)
            ) {
                Text(
                    text = "STATS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black
                )
            }
        }
    }
}
