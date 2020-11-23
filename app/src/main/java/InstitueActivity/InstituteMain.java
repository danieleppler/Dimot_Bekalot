package InstitueActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.entryActivities.dimot_bekalot.R;

public class InstituteMain extends AppCompatActivity implements View.OnClickListener {
    Button update, watching;
    TextView institute_name;
    Institute insti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_main);

        /* name of institute */
        institute_name = (TextView)findViewById(R.id.name_institute);
        institute_name.setText(insti.getName());

        update = (Button)findViewById(R.id.update_queue);
        watching = (Button)findViewById(R.id.watch_queue);

    }

    @Override
    public void onClick(View v) {
        if(update == v){ // go to update queue
//            Intent update_in = new Intent(this, ----);
//            startActivity(update_in);
        }
        else if (watching == v) { // just watch no update
//            Intent watch_in = new Intent(this, -----);
//            startActivity(watch_in);

        }
    }
}
