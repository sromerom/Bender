import java.util.Arrays;
import java.util.List;

class Bender {
    private Mapa mapa;

    public enum Direccions {
        SOUTH, EAST, NORTH, WEST
    }

    private Direccions[] prioritatInicial = new Direccions[]{Direccions.SOUTH, Direccions.EAST, Direccions.NORTH, Direccions.WEST};
    private Direccions[] inversor = new Direccions[]{Direccions.NORTH, Direccions.WEST, Direccions.SOUTH, Direccions.EAST};

    // Constructor: ens passen el mapa en forma d'String
    public Bender(String mapa) {
        this.mapa = new Mapa(mapa);
    }

    // Navegar fins a l'objectiu («$»).
    // El valor retornat pel mètode consisteix en una cadena de
    // caràcters on cada lletra pot tenir
    // els valors «S», «N», «W» o «E»,
    // segons la posició del robot a cada moment.
    public String run() {
        return "";
    }

    public String bestRun() {
        return "";
    }
}

class Mapa {
    private String mapaString;
    private String[][] mapa;
    private Vector posicioBender;
    private Vector objectiu;
    private List<Vector> inversors;
    private List<Vector> teleportadors;

    public Mapa(String mapa) {
        int totalCaracters = mapa.length();
        int altura = 1;
        int amplada = 0;


        while (mapa.charAt(amplada) == '#') {
            amplada++;
        }

        while (amplada * altura <= totalCaracters) {
            altura++;
        }
        altura -= 1;

        this.mapaString = mapa;
        System.out.println("amplada " + amplada);
        System.out.println("altura: " + altura);
        this.mapa = new String[altura][amplada];
        omplirMapa();
        System.out.println(Arrays.deepToString(this.mapa));
    }

    private void omplirMapa() {
        int aux = 0;
        System.out.println(mapaString.charAt(7));
        for (int i = 0; i < this.mapa.length; i++) {
            for (int j = 0; j < this.mapa[i].length; j++) {
                this.mapa[i][j] = Character.toString(mapaString.charAt(aux));
                aux++;
            }
            aux++;
        }
    }
}

class Vector {
    private int x = 0;
    private int y = 0;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}