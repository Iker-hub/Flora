package es.ikergarciagutierrez.accdat.flora.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import es.ikergarciagutierrez.accdat.flora.R;
import es.ikergarciagutierrez.accdat.flora.model.entity.Flora;
import es.ikergarciagutierrez.accdat.flora.viewmodel.EditFloraViewModel;
import es.ikergarciagutierrez.accdat.flora.viewmodel.MainActivityViewModel;

public class EditFloraActivity extends AppCompatActivity {

    private Context context;
    private Flora flora;
    private EditFloraViewModel efvm;

    private ImageView ivFlora;
    private EditText etNombre, etFamilia, etIdentificacion, etAltitud, etHabitat, etFitosociologia,
            etBiotipo, etBioReproductiva, etFloracion, etFructificacion, etExpSexual, etPolinizacion,
            etDispersion, etNumCromosomatico, etRepAsexual, etDistribucion, etBiologia, etDemografia,
            etAmenazas, etMedPropuestas;
    private TextView tvMoreInfo;
    private Button btBorrar, btEditar, btCancelarEdicion, btGuardarEdicion;

    private String ivFloraURL = "https://informatica.ieszaidinvergeles.org:10008/ad/felixRLDFApp/public/api/imagen/";

    private String adBorrarTitulo = "¿Borrar X?";
    private String adEditarTitulo = "¿Editar X?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flora);
        context = this;
        initialize();
    }

    private void initialize() {

        efvm = new ViewModelProvider(this).get(EditFloraViewModel.class);

        ivFlora = findViewById(R.id.ivFlora);
        etNombre = findViewById(R.id.etFloraNombre);
        etFamilia = findViewById(R.id.etFloraFamilia);
        etIdentificacion = findViewById(R.id.etFloraIdentificacion);
        etAltitud = findViewById(R.id.etFloraAltitud);
        etHabitat = findViewById(R.id.etFloraHabitat);
        etFitosociologia = findViewById(R.id.etFloraFitosociologia);
        etBiotipo = findViewById(R.id.etFloraBiotipo);
        etBioReproductiva = findViewById(R.id.etFloraBiologiaReproductiva);
        etFloracion = findViewById(R.id.etFloraFloracion);
        etFructificacion = findViewById(R.id.etFloraFructificacion);
        etExpSexual = findViewById(R.id.etFloraExpresionSexual);
        etPolinizacion = findViewById(R.id.etFloraPolinizacion);
        etDispersion = findViewById(R.id.etFloraDispersion);
        etNumCromosomatico = findViewById(R.id.etFloraNumCromosomatico);
        etRepAsexual = findViewById(R.id.etFloraReproduccionAsexual);
        etDistribucion = findViewById(R.id.etFloraDistribucion);
        etBiologia = findViewById(R.id.etFloraBiologia);
        etDemografia = findViewById(R.id.etFloraDemografia);
        etAmenazas = findViewById(R.id.etFloraAmenazas);
        etMedPropuestas = findViewById(R.id.etFloraMedidasPropuestas);

        tvMoreInfo = findViewById(R.id.tvMoreInfo);

        btBorrar = findViewById(R.id.btBorrar);
        btEditar = findViewById(R.id.btEditar);
        btCancelarEdicion = findViewById(R.id.btCancelarEdicion);
        btGuardarEdicion = findViewById(R.id.btGuardarEdicion);

        setFlora();
        deshabilitarEdicion();

        defineTextViewMoreInfo();
        defineButtonBorrar();
        defineButtonEditar();
        defineButtonCancelarEdicion();
        defineButtonGuardarEdicion();
    }

    private void defineTextViewMoreInfo() {
        tvMoreInfo.setOnClickListener(view -> {
            if (etNombre.getText().toString().isEmpty()) {
                Toast.makeText(context, R.string.toast_errorWiki, Toast.LENGTH_LONG).show();
            } else {
                String urlWikipedia = "https://es.wikipedia.org/wiki/" + etNombre.getText().toString().trim();
                Uri uri = Uri.parse(urlWikipedia);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void defineButtonBorrar() {
        btBorrar.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle(adBorrarTitulo.replace("X", etNombre.getText()))
                    .setMessage(R.string.alertDialogBorrar_message)
                    .setPositiveButton(R.string.alertDialog_confirmar, (dialog, which) -> {
                        // Borramos de la bd
                        efvm.deleteFlora(flora.getId());
                        Toast.makeText(context, R.string.toast_borrarFlora, Toast.LENGTH_LONG).show();
                        finish();
                    })
                    .setNegativeButton(R.string.alertDialog_cancelar, (dialog, which) -> {
                        dialog.cancel();
                    })
                    .show();
        });
    }

    private void defineButtonEditar() {
        btEditar.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle(adEditarTitulo.replace("X", etNombre.getText()))
                    .setMessage(R.string.alertDialogBorrar_message)
                    .setPositiveButton(R.string.alertDialog_confirmar, (dialog, which) -> {
                        habilitarEdicion();
                    })
                    .setNegativeButton(R.string.alertDialog_cancelar, (dialog, which) -> {
                        dialog.cancel();
                    })
                    .show();
        });
    }

    private void defineButtonCancelarEdicion() {
        btCancelarEdicion.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.alertDialogCancelarEdicion_title)
                    .setMessage(R.string.alertDialogCancelarEdicion_message)
                    .setPositiveButton(R.string.alertDialog_confirmar, (dialog, which) -> {
                        deshabilitarEdicion();
                        setFlora();
                    })
                    .setNegativeButton(R.string.alertDialog_cancelar, (dialog, which) -> {
                        dialog.cancel();
                    })
                    .show();
        });
    }

    private void defineButtonGuardarEdicion() {
        btGuardarEdicion.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.alertDialogGuardarEdicion_title)
                    .setMessage(R.string.alertDialogGuardarEdicion_message)
                    .setPositiveButton(R.string.alertDialog_confirmar, (dialog, which) -> {
                        deshabilitarEdicion();
                        // Guardar cambios
                        if (areFieldsEmpty()) {
                            efvm.editFlora(flora.getId(), getFlora());
                            Toast.makeText(context, R.string.toast_editarFlora, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, R.string.toast_fieldsEmpty, Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton(R.string.alertDialog_cancelar, (dialog, which) -> {
                        dialog.cancel();
                    })
                    .show();
        });
    }

    private void habilitarEdicion() {

        btBorrar.setVisibility(View.GONE);
        btEditar.setVisibility(View.GONE);
        btCancelarEdicion.setVisibility(View.VISIBLE);
        btGuardarEdicion.setVisibility(View.VISIBLE);

        etNombre.setEnabled(true);
        etFamilia.setEnabled(true);
        etIdentificacion.setEnabled(true);
        etAltitud.setEnabled(true);
        etHabitat.setEnabled(true);
        etFitosociologia.setEnabled(true);
        etBiotipo.setEnabled(true);
        etBioReproductiva.setEnabled(true);
        etFloracion.setEnabled(true);
        etFructificacion.setEnabled(true);
        etExpSexual.setEnabled(true);
        etPolinizacion.setEnabled(true);
        etDispersion.setEnabled(true);
        etNumCromosomatico.setEnabled(true);
        etRepAsexual.setEnabled(true);
        etDistribucion.setEnabled(true);
        etBiologia.setEnabled(true);
        etDemografia.setEnabled(true);
        etAmenazas.setEnabled(true);
        etMedPropuestas.setEnabled(true);
    }

    private void deshabilitarEdicion() {

        btBorrar.setVisibility(View.VISIBLE);
        btEditar.setVisibility(View.VISIBLE);
        btCancelarEdicion.setVisibility(View.GONE);
        btGuardarEdicion.setVisibility(View.GONE);

        Picasso.get().load(ivFloraURL + flora.getId() + "/flora").into(ivFlora);

        etNombre.setEnabled(false);
        etFamilia.setEnabled(false);
        etIdentificacion.setEnabled(false);
        etAltitud.setEnabled(false);
        etHabitat.setEnabled(false);
        etFitosociologia.setEnabled(false);
        etBiotipo.setEnabled(false);
        etBioReproductiva.setEnabled(false);
        etFloracion.setEnabled(false);
        etFructificacion.setEnabled(false);
        etExpSexual.setEnabled(false);
        etPolinizacion.setEnabled(false);
        etDispersion.setEnabled(false);
        etNumCromosomatico.setEnabled(false);
        etRepAsexual.setEnabled(false);
        etDistribucion.setEnabled(false);
        etBiologia.setEnabled(false);
        etDemografia.setEnabled(false);
        etAmenazas.setEnabled(false);
        etMedPropuestas.setEnabled(false);
    }

    private boolean areFieldsEmpty() {
        if (etNombre.getText().toString().isEmpty() || etFamilia.getText().toString().isEmpty() ||
                etIdentificacion.getText().toString().isEmpty() || etAltitud.getText().toString().isEmpty() ||
                etHabitat.getText().toString().isEmpty() || etFitosociologia.getText().toString().isEmpty() ||
                etBiotipo.getText().toString().isEmpty() || etBioReproductiva.getText().toString().isEmpty() ||
                etFloracion.getText().toString().isEmpty() || etFructificacion.getText().toString().isEmpty() ||
                etExpSexual.getText().toString().isEmpty() || etPolinizacion.getText().toString().isEmpty() ||
                etDispersion.getText().toString().isEmpty() || etNumCromosomatico.getText().toString().isEmpty() ||
                etRepAsexual.getText().toString().isEmpty() || etDistribucion.getText().toString().isEmpty() ||
                etBiologia.getText().toString().isEmpty() || etDemografia.getText().toString().isEmpty() ||
                etAmenazas.getText().toString().isEmpty() || etMedPropuestas.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    private Flora getFlora() {

        flora = new Flora();

        flora.setNombre(etNombre.getText().toString());
        flora.setFamilia(etFamilia.getText().toString());
        flora.setIdentificacion(etIdentificacion.getText().toString());
        flora.setAltitud(etAltitud.getText().toString());
        flora.setHabitat(etHabitat.getText().toString());
        flora.setFitosociologia(etFitosociologia.getText().toString());
        flora.setBiotopo(etBiotipo.getText().toString());
        flora.setBiologia_reproductiva(etBioReproductiva.getText().toString());
        flora.setFloracion(etFloracion.getText().toString());
        flora.setFructificacion(etFructificacion.getText().toString());
        flora.setExpresion_sexual(etExpSexual.getText().toString());
        flora.setPolinizacion(etPolinizacion.getText().toString());
        flora.setDispersion(etDispersion.getText().toString());
        flora.setNumero_cromosomico(etNumCromosomatico.getText().toString());
        flora.setReproduccion_asexual(etRepAsexual.getText().toString());
        flora.setDistribucion(etDistribucion.getText().toString());
        flora.setBiologia(etBiologia.getText().toString());
        flora.setDemografia(etDemografia.getText().toString());
        flora.setAmenazas(etAmenazas.getText().toString());
        flora.setMedidas_propuestas(etMedPropuestas.getText().toString());

        return flora;
    }

    private void setFlora() {

        Bundle bundle = getIntent().getExtras();
        flora = bundle.getParcelable("idFlora");

        etNombre.setText(flora.getNombre());
        etFamilia.setText(flora.getFamilia());
        etIdentificacion.setText(flora.getIdentificacion());
        etAltitud.setText(flora.getAltitud());
        etHabitat.setText(flora.getHabitat());
        etFitosociologia.setText(flora.getFitosociologia());
        etBiotipo.setText(flora.getBiotopo());
        etBioReproductiva.setText(flora.getBiologia_reproductiva());
        etFloracion.setText(flora.getFloracion());
        etFructificacion.setText(flora.getFructificacion());
        etExpSexual.setText(flora.getExpresion_sexual());
        etPolinizacion.setText(flora.getPolinizacion());
        etDispersion.setText(flora.getDispersion());
        etNumCromosomatico.setText(flora.getNumero_cromosomico());
        etRepAsexual.setText(flora.getReproduccion_asexual());
        etDistribucion.setText(flora.getDistribucion());
        etBiologia.setText(flora.getBiologia());
        etDemografia.setText(flora.getDemografia());
        etAmenazas.setText(flora.getAmenazas());
        etMedPropuestas.setText(flora.getMedidas_propuestas());
    }

}