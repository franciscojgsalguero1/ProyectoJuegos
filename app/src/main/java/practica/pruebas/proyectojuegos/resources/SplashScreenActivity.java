package practica.pruebas.proyectojuegos.resources;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import practica.pruebas.proyectojuegos.MainActivity;
import practica.pruebas.proyectojuegos.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Obtener referencia al título
        View titleFadeIn = findViewById(R.id.title_top);
        titleFadeIn.setAlpha(0);
        titleFadeIn.animate().alpha(1).setDuration(2000);

        // Crear animación de alpha con ObjectAnimator
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(titleFadeIn, "alpha", 0f, 1f);
        fadeIn.setDuration(1000); // Duración: 1 segundo
        fadeIn.start(); // Iniciar animación

        // Obtener referencias a las imágenes
        ImageView image1 = findViewById(R.id.splash1);
        ImageView image2 = findViewById(R.id.splash2);
        ImageView image3 = findViewById(R.id.splash3);
        ImageView image4 = findViewById(R.id.splash4);

        // Crear animación rotatoria
        RotateAnimation rotateAnimation = new RotateAnimation(
                0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000); // Duración de 2 segundos
        rotateAnimation.setRepeatCount(RotateAnimation.INFINITE);

        // Aplicar animación a cada imagen
        image1.startAnimation(rotateAnimation);
        image2.startAnimation(rotateAnimation);
        image3.startAnimation(rotateAnimation);
        image4.startAnimation(rotateAnimation);

        // Configurar un delay para cambiar a la MainActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            intent.putExtra("fromSplash", true); // Indicador para evitar repetir el Splash
            startActivity(intent);
            finish();
        }, 100); // 100 segundos de duración del SplashScreen
    }


}
