package med.voll.api.infra.errores;

public class ValidacionDeIntregridad extends RuntimeException {
    public ValidacionDeIntregridad(String s) {
        super(s);
    }
}
