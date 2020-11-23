package InstitueActivity;

public class Institute {
    private String _name, _location;

    Institute(String name, String location){
        this._name = name;
        this._location = location;
    }

    String getName(){
        return this._name;
    }
    String getLocation(){
        return this._location;
    }
}
