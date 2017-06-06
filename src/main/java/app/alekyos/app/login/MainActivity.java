package app.alekyos.app.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    //Se declaran los objetos
    EditText etUsuario, etContra;
    Button btnIngresar;
    JSONArray ja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se instancian los objetos
        etUsuario = (EditText)findViewById(R.id.etUsuario);
        etContra = (EditText)findViewById(R.id.etContra);
        btnIngresar = (Button)findViewById(R.id.btnIngresar);


        //Se pondra un click listener al boton
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"En btnIngresar", Toast.LENGTH_SHORT).show();

                //Al presionar el boton, el ira a un metodo que es consultar contraseña
                ConsultaPass("http://10.0.3.2/ejemplologin/consultarusuario.php?apodo="+etUsuario.getText().toString());
                //saca el archiv de aqui:  C:\xampp\htdocs\ejemplologin
                /*La direccion puede ser local si esta  usando un dispositivo externo, y para llegar a este se debe:
                CD a la internet, abrir centro de redes. Aqui buscar conexiones y abrir la coneccion ( CI)
                Ahora abrir detalles y buscar Direccion Ipv4 y a la que esta a la lado es nuestra direccion IP
                 */
            }
        });
    }


    private void ConsultaPass(String URL) {

        Log.i("url",""+URL);
        //Aqui para poder importar se debe en manifest escribir: <uses-permission android:name="android.permission.INTERNET" />
        //Luego en gradle scripst/buid.gradle(module), en dependeces, justo al final lo siguiente: compile 'com.android.volley:volley:1.0.0'
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Aqui coge la respuesta de servicio web y la convierte en jsonarray
                    ja = new JSONArray(response);
                    //Cogera la contraseña que encontro
                    String contra = ja.getString(0);

                    //Cogera la contraseña introducida y verificara si es igual a la contraseña de la base de datos
                    if(contra.equals(etContra.getText().toString())){

                        Toast.makeText(getApplicationContext(),"Bienvenido",Toast.LENGTH_SHORT).show();
                        /*Intent intent = new Intent(MainActivity.this, pantallaBienvenido.class);
                        startActivity(intent);*/
                    }else{
                        Toast.makeText(getApplicationContext(),"verifique su contraseña",Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(getApplicationContext(),"El usuario no existe en la base de datos",Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);



    }
}
