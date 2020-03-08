import java.util.*;

class Bender {
    private final Mapa mapa;
    private Mapa mapaActualitzat;

    public enum Coordenades {
        S, E, N, W
    }

    private Map<Coordenades, Vector> mapeig = new HashMap<Coordenades, Vector>();
    private Coordenades[] prioritatActual = new Coordenades[Coordenades.values().length];

    // Constructor: ens passen el mapa en forma d'String
    public Bender(String mapa) {
        this.mapa = new Mapa(mapa);
        this.mapaActualitzat = new Mapa(mapa);
        mapeig.put(Coordenades.S, new Vector(1, 0));
        mapeig.put(Coordenades.E, new Vector(0, 1));
        mapeig.put(Coordenades.N, new Vector(-1, 0));
        mapeig.put(Coordenades.W, new Vector(0, -1));
        prioritatActual[0] = Coordenades.S;
        prioritatActual[1] = Coordenades.E;
        prioritatActual[2] = Coordenades.N;
        prioritatActual[3] = Coordenades.W;
    }

    // Navegar fins a l'objectiu («$»).
    // El valor retornat pel mètode consisteix en una cadena de
    // caràcters on cada lletra pot tenir
    // els valors «S», «N», «W» o «E»,
    // segons la posició del robot a cada moment.
    public String run() {
        String resultat = "";
        Coordenades direccioBender = Coordenades.S;
        int numeroPosicio = 0;
        Vector maximRecorregut = new Vector(0, 0);
        Vector anteriorPosicioBender = new Vector(0, 0);
        int teleportadorsAgafats = 0;
        boolean shaMogut = false;

        //Si directament no hi trobam cap objectiu ($) ni cap robot o inici (X), retornarem null directament.
        if (mapaActualitzat.getObjectiu() == null || mapaActualitzat.getPosicioBender() == null) {
            return null;
        }

        //Avançarem bender sempre i quan la posició del Bender no sigui la mateixa a la del objectiu. Si es igual, voldra dir que ja arribat al destí i que haurem de retornar totes les direccions
        //guardades fins ara.
        while (!mapaActualitzat.getPosicioBender().equals(mapaActualitzat.getObjectiu())) {

            //Aquesta condició ens permet saber quantes vegades ha estat Bender sobre un mateix eix (ja sigui X o Y). Feim això per determinar si un mapa es possible de fer o no (mapes impossibles).
            //Nomes hi entrarem sempre i quan bender s'hagui mogut. (Ha canviat de coordenades en el mapa)
            if (shaMogut) {

                //Si en l'anterior coordenada Y de la posicio anterior de Bender es igual que l'actual, voldra dir que segueix en el mateix eix, per tant augmentarem en un la coordenada Y
                if (anteriorPosicioBender.getY() == mapaActualitzat.getPosicioBender().getY()) {
                    maximRecorregut.setY(maximRecorregut.getY() + 1);
                } else { // Si no es compleix la condició anterior, voldra dir que ja no esta el mateix eix Y i per tant "resetarem" la coordenada Y, la posarem un altre vegada a 0.
                    maximRecorregut.setY(0);
                }

                //Feim exactament el mateix per el eix X. Si l'anterior es igual a l'actual augmentam i si no ho es tornam a posar a 0.
                if (anteriorPosicioBender.getX() == mapaActualitzat.getPosicioBender().getX()) {
                    maximRecorregut.setX(maximRecorregut.getX() + 1);
                } else {
                    maximRecorregut.setX(0);
                }
            }

            //Si es dona el cas que la suma que s'ha anat fent abans es mes gran que la propia longitud * 2 del mapa, voldra dir que bender es troba en un bucle sobre un mateix eix i que haurem de
            // retornam null. Ho feim tant per l'eix X i Y.
            if (maximRecorregut.getX() > (mapaActualitzat.getMapa().length * 2) && maximRecorregut.getX() > (mapaActualitzat.getMapa()[0].length * 2)
                    || maximRecorregut.getY() > (mapaActualitzat.getMapa().length * 2) && maximRecorregut.getY() > (mapaActualitzat.getMapa()[0].length * 2)) {
                return null;
            }

            //L'anterior condició per determinar si un mapa es impossible no abarca totes les possibilitats. Si es dona el cas que es crea un bucle de teleports, la gestió d'eixos d'abans no
            //funciona, per tant toca crear una variable que es vagi incrementant cada vegada que bender agafa un teleport.

            //I si la suma total dels teleports agafats es major a la llargaria del mapa * 2, voldra dir que ha agafat mes teleports del possibles, per tant retornarem null
            if (teleportadorsAgafats > mapaActualitzat.getMapa().length * 2) {
                return null;
            }

            //Aconseguirem l'objecte següent depenent de la direcció actual de Bender. Segons la direcció de Bender, s'agafara una coordenada del contenidor Map i fara
            // la corresponent operacio gracies al mètode opera(). Aquesta operacio ens retorna on es situa el següent objecte.
            char objecteSeguent = mapaActualitzat.getMapa()[mapaActualitzat.getPosicioBender().opera(mapeig.get(direccioBender)).getX()][mapaActualitzat.getPosicioBender().opera(mapeig.get(direccioBender)).getY()];

            //Si l'objecte següent es un espai un $ o una X...
            if (objecteSeguent == ' ' || objecteSeguent == '$' || objecteSeguent == 'X') {
                //Guardam l'actual posicio de Bender a la variable resultat.
                resultat = resultat + direccioBender.toString();
                //I abans de moure Bender guardam quina es la actual posicio de Bender, que posteriorment sera l'anterior.
                anteriorPosicioBender = mapaActualitzat.getPosicioBender();

                //Avançam bender segons la seva direcció exactament igual que abans. Aconseguim les coordenades del Map segons la direcció que tengui Bender i calculam aquesta coordenada sobre
                //la posicio actual de Bender.
                mapaActualitzat.setPosicioBender(mapaActualitzat.getPosicioBender().opera(mapeig.get(direccioBender)));
                shaMogut = true;
                numeroPosicio = 0; //Important reiniciar aquesta variable a 0 per a que es pugui recorrer el array de prioritats en cas de trobar una paret.

                //Si es troba una paret, anira recorrent l'actual array de prioritat fins que amb una direcció no trobi una pared
            } else if (objecteSeguent == '#') {

                //Aquesta condicio ens permet saber si Bender es troba tancat i no pot avançar de qualsevol manera. Si això passa toca retornar null
                if (numeroPosicio == 4) {
                    return null;
                }
                //Cada vegada que entri a aquesta condició canviara la seva direcció.Per a que això funcioni haurem d'augmentar la variable numeroPosicio en un.
                direccioBender = prioritatActual[numeroPosicio];
                numeroPosicio++;

                //Com que Bender realment no s'ha mogut, es important assignar la variable shaMogut com false, per a que no conti que es troba sobre el mateix eix. (condicio mapes impossibles)
                shaMogut = false;

                //Si es troba un teleport, haura de trobar quin es el teleport mes a prop i avançar Bender cap aquell teleport
            } else if (objecteSeguent == 'T') {
                resultat += direccioBender.toString();
                teleportadorsAgafats++;
                mapaActualitzat.setPosicioBender(mapaActualitzat.getPosicioBender().opera(mapeig.get(direccioBender))); // Avançam la posicio de Bender
                Vector posicioNovaBender = aconseguiexTeleporter(mapaActualitzat.getPosicioBender()); //Aconseguim a quina posicio es teleportara (gracies al metode aconseguiexTeleporter)
                mapaActualitzat.setPosicioBender(posicioNovaBender); //I tornam a avançar Bender, pero aquesta vegada directament cap a la nova posicioCalculada, sense fer servir el metode opera()
                shaMogut = true; // I indicam que bender s'ha mogut.

                //Si es troba un inverter, haurem d'avançar Bender i canviar la prioritat de Bender
            } else if (objecteSeguent == 'I') {
                resultat += direccioBender; // Com cada vegada que s'avança Bender, guardam la direccio en la que ha avançat.
                anteriorPosicioBender = mapaActualitzat.getPosicioBender(); //Tornam a aconseguir el que sera l'anterior posicio que ha estat Bender.
                mapaActualitzat.setPosicioBender(mapaActualitzat.getPosicioBender().opera(mapeig.get(direccioBender))); //Avançam Bender.
                inverteix(); //I per canviar la prioritat de S, E, N, W a N, W, S, E farem servir el metode inverteix que simplement canvia el contigut del array prioritatActual a la nova prioritat.
                shaMogut = true; //I si es mou Bender, posam a true shaMogut.
            }
        }

        return resultat;
    }

    public int bestRun() {
        boolean[][] casellesActivades = new boolean[mapaActualitzat.getMapa().length][mapaActualitzat.getMapa()[0].length];
        Queue<Estat> adjacents = new LinkedList<>();
        Estat posicioInicial = new Estat(mapaActualitzat.getPosicioBender().getX(), mapaActualitzat.getPosicioBender().getY(), 0);

        //Ficam la primera posicio inicial a la cua.
        adjacents.offer(posicioInicial);

        //El array bidimensional de booleans, en un estat inicial han d'estar en false, per tant...
        for (int i = 0; i < casellesActivades.length; i++) {
            for (int j = 0; j < casellesActivades[0].length; j++) {
                casellesActivades[i][j] = false;
            }
        }

        //Per incrementar les coordenades X i Y utilitzarem dos arrays.
        int[] incrementarX = new int[]{0, 0, 1, -1};
        int[] incrementarY = new int[]{1, -1, 0, 0};

        //Mentres la cua no estigui buida...
        while (!adjacents.isEmpty()) {
            Estat actual = adjacents.poll(); // Aconseguim l'actual estat adjacent i l'eliminam

            //Si l'actual Estat adjacent equival al ($) voldra dir que ha trobat el final i haurem de retornar l'actual distancia del Estat adjacent
            if (mapaActualitzat.getMapa()[actual.getX()][actual.getY()] == '$') {
                return actual.getD();
            }

            //Si no ha trobat el final haura de seguir. Marcam com que l'actual Estat adjacent ja ha passat per alla. Ho feim assignant a la posicio del array de boolean un "true".
            casellesActivades[actual.getX()][actual.getY()] = true;

            //Ara toca veure els següent adjacent de l'actual Estat. Farem un bucle per aconseguir aquest quatre adjacents (nomes podem aconseguir 4 adjacents).

            //Recorrem adjacents
            for (int i = 0; i < 4; i++) {
                int coordenadaXAdjacent = incrementarX[i] + actual.getX();
                int coordenadaYAdjacent = incrementarY[i] + actual.getY();

                //Si troba un teleport haurem de tractar de manera diferents els estats...
                if (mapaActualitzat.getMapa()[coordenadaXAdjacent][coordenadaYAdjacent] == 'T') {
                    Vector actualTeleporter = aconseguiexTeleporter(new Vector(coordenadaXAdjacent, coordenadaYAdjacent)); //Aconseguim al teleport que anira
                    Estat adjacent = new Estat(actualTeleporter.getX(), actualTeleporter.getY(), actual.getD() + 1); //I feim que els adjacents del teleport que ha agafat siguin els mateixos que l'altre teleport.
                    adjacents.offer(adjacent); // Una vegada s'ha trobat els adjacent, el ficam a la cua.

                    //Si les coordenades de actual d'adjacent son menors al tamany del tamamy del mapa, no es una pared i no s'ha passat mai per alla...
                } else if (coordenadaXAdjacent < mapaActualitzat.getMapa().length
                        && coordenadaYAdjacent < mapaActualitzat.getMapa()[0].length && mapaActualitzat.getMapa()[coordenadaXAdjacent][coordenadaYAdjacent] != '#'
                        && !casellesActivades[coordenadaXAdjacent][coordenadaYAdjacent]) {
                    //Doncs cream un nou estat de l'adjacent i el ficam a la cua de estats
                    Estat adjacent = new Estat(coordenadaXAdjacent, coordenadaYAdjacent, actual.getD() + 1);
                    adjacents.offer(adjacent);
                }
            }
        }

        //Si ha arribat fins aqui, voldra dir que no ha aconseguit arribar al seu objectiu, per tant en el meu cas retornare un -1.
        return -1;
    }

    //Métode que va invertint l'array prioritatActual cada vegada que es crida. Simplement va afegint les dos ultimes coordenades al principi.
    public void inverteix() {
        Coordenades t = prioritatActual[2];
        Coordenades t2 = prioritatActual[3];
        this.prioritatActual[2] = prioritatActual[0];
        this.prioritatActual[3] = prioritatActual[1];
        this.prioritatActual[0] = t;
        this.prioritatActual[1] = t2;

    }

    //Métode que ens permet aconseguir quin es el teleport que ha d'anar Bender. Retorna un Vector amb la posicio del teleport que ha de teleportar-se
    public Vector aconseguiexTeleporter(Vector teleport) {
        Iterator<Vector> itr = mapaActualitzat.getTeleportadors().iterator();
        int selector = 0;
        int aux = 0;
        double distanciaActual = 0;
        double distanciaMinima = 0;

        //Iteram tots els teleports que hi ha al mapa
        while (itr.hasNext()) {
            //Guardam el Vector de l'actual teleport
            Vector actual = itr.next();

            //Si el teleport actual de la llista es exactament al teleport que ha agafat no hem de calcular la seva distancia, per tant no hi entra
            if (!actual.equals(teleport)) {
                //Calculam la distancia actual gracies al metode distanciaEuclidiana().
                distanciaActual = actual.distanciaEuclidiana(teleport);

                //Si es la primera vegada posarem que la distancia minima sigui la de l'actual teleport.
                if (distanciaMinima == 0) {
                    distanciaMinima = distanciaActual;
                    selector = aux;

                    //Si la distancia de l'actual teleport ha sortit menor distancia la distancia tenim guardada (distanciaMinima), entrarem i assignarem que la distancia mes petita fins ara
                    //es l'actual i que el selector es el valor actual de aux. Així si volem accedir al teleport mes a prop ho podrem fer gràcies al selector.
                } else if (distanciaActual < distanciaMinima) {
                    distanciaMinima = distanciaActual;
                    selector = aux;

                    //Si es dona el cas que la distanciaActual es la distanciaMinima, haurem d'aplicar la prioritat en forma de rellotge. S'agafaran abans els teleports que estiguin mes a prop de les 12 d'un
                    //rellotge, per tant...
                } else if (distanciaActual == distanciaMinima) {

                    //Aconseguim el que sera el teleport següent.
                    Vector suposatTeleport = mapaActualitzat.getTeleportadors().get(selector);

                    //Si el actual teleport es major al suposat teleport, voldra dir que esta a la dreta del eix
                    if (actual.getY() > suposatTeleport.getY()) {
                        distanciaMinima = distanciaActual;
                        selector = aux;
                    } else if (actual.getY() == suposatTeleport.getY() && actual.getY() < teleport.getX() && actual.getX() > suposatTeleport.getX()) {
                        distanciaMinima = distanciaActual;
                        selector = aux;
                    }
                }
            }
            aux++;
        }

        return new Vector(mapaActualitzat.getTeleportadors().get(selector).getX(), mapaActualitzat.getTeleportadors().get(selector).getY());
    }
}

//La classe mapa ens permet transformar un string d'una mapa a un objecte. Amb aixo aconseguim saber per exemple on comença Bender, on ha d'acabar, tots els teleports i inversors que hi ha en el mapa i
//segurament el més important, aconseguir en format d'array bidimensional el String mapa
class Mapa {
    private String mapaString;
    private char[][] mapa;
    private Vector posicioBender;
    private Vector objectiu;
    private List<Vector> inversors = new ArrayList<>();
    private List<Vector> teleportadors = new ArrayList<>();

    //Al constructor, aconseguim de quin tamany és el mapa (altura i amplada) a partir del String que ens passen i omplim l'array bidimensional amb tots els elements del mapa
    public Mapa(String mapa) {
        int altura = 0;
        int amplada = 0;

        int actualAmplada = 0;
        //Aconseguim l'amplada del mapa
        for (int i = 0; i < mapa.length(); i++) {
            if (mapa.charAt(i) != '\n') {
                actualAmplada++;
            } else { // Si el mapa es irregular, agafarem l'amplada maxima de tot el mapa
                if (actualAmplada > amplada) {
                    amplada = actualAmplada;
                }
                actualAmplada = 0;
            }
        }

        //Aconseguim l'altura del mapa
        for (int i = 0; i < mapa.length(); i++) {
            if (mapa.charAt(i) == '\n') { // Per cada salt de linea que trobem, voldra dir una altura mes
                altura++;
            }
        }
        altura++; //Com que l'ultima linea del string no té cap salt de linea, hem de sumar-li un a la altura

        //Es guardara el mapa en format String i es creara el array bidimensional amb el tamanys que hem aconseguit adalt
        this.mapaString = mapa;
        this.mapa = new char[altura][amplada];

        //I omplim la array bidimensional dels elements que hi ha al mapa amb el metode omplirMapa()
        omplirMapa();
    }

    //El metode omplirMapa ens permet omplir l'array bidimensional amb tots i cada uns dels elements que hi ha al string
    private void omplirMapa() {
        int indexElement = 0;
        boolean afegixAuto = false;

        //Anam recorrent el array bidimensional i anam afegint tots els caracters del string. Anirem aconseguit els caracters del String amb la variable "indexElement".
        for (int i = 0; i < this.getMapa().length; i++) {

            //En cada columna que avançem augmentarem el index i tornarem la variable afegiexAuto a false. Es important nomes augmentar el index quan el boolea estigui en false i no sigui
            //la primera vegada que s'entra al bucle.
            if (!afegixAuto && i != 0) {
                indexElement++;
            }
            afegixAuto = false;

            for (int j = 0; j < this.getMapa()[i].length; j++) {

                //Si el mapa es irregular, s'haura d'aturar manualment el bucle...
                if (indexElement == this.mapaString.length()) {
                    break;
                }

                //Aconseguim l'actual element del mapa
                char actualElement = this.mapaString.charAt(indexElement);

                //Si el mapa es irregular, pot ser que trobem una fila al string mes petita que el tamany maxim del array, per tant, haurem posar en true la variable afegeixAuto per tal d'anar omplint
                //els espais restants. (fins a arribar al tamany de l'array).
                if (actualElement == '\n') {
                    indexElement++;
                    afegixAuto = true;
                    continue;
                }

                //Obviam les posicions restants del array, ja que en el string ja s'ha arribat al final de la fila (aixo nomes passa en mapes que son irregulars).
                if (afegixAuto) {
                    continue;
                }

                //Si el caracter actual es un element del mapa important, ho assignarem al atributs de la classe. Aixo només passa per X, I,T i $
                if (actualElement == 'X') {
                    this.setPosicioBender(new Vector(i, j));
                } else if (actualElement == 'I') {
                    this.getInversors().add(new Vector(i, j));
                } else if (actualElement == 'T') {
                    this.getTeleportadors().add(new Vector(i, j));
                } else if (actualElement == '$') {
                    this.setObjectiu(new Vector(i, j));
                }

                //Afegim l'element actual a la corresponent posicio de l'array bidimensional i augmentam al següent element del string mapa (indexElement)
                this.mapa[i][j] = actualElement;
                indexElement++;
            }
        }
    }

    //Metode toString. Simplement ens retorna en format String el mapa.
    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < this.mapa.length; i++) {
            res += "\n";
            for (int j = 0; j < this.mapa[i].length; j++) {
                res += this.mapa[i][j];
            }
        }
        return res;
    }


    //Getters and Setters
    public String getMapaString() {
        return mapaString;
    }

    public void setMapaString(String mapaString) {
        this.mapaString = mapaString;
    }

    public char[][] getMapa() {
        return mapa;
    }

    public void setMapa(char[][] mapa) {
        this.mapa = mapa;
    }

    public Vector getPosicioBender() {
        return posicioBender;
    }

    public void setPosicioBender(Vector posicioBender) {
        this.posicioBender = posicioBender;
    }

    public Vector getObjectiu() {
        return objectiu;
    }

    public void setObjectiu(Vector objectiu) {
        this.objectiu = objectiu;
    }

    public List<Vector> getInversors() {
        return inversors;
    }

    public void setInversors(List<Vector> inversors) {
        this.inversors = inversors;
    }

    public List<Vector> getTeleportadors() {
        return teleportadors;
    }

    public void setTeleportadors(List<Vector> teleportadors) {
        this.teleportadors = teleportadors;
    }
}

//Classe Vector ens permet treballar d'una forma mes facil i intuitiva les coordenades X i Y d'un element qualsevol. Per tant un vector constarà nomès d'una X i una Y.
class Vector {
    private int x = 0;
    private int y = 0;

    //Constructor que carrega les coordenades que ens passen
    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //El metode opera ens permet operar entre dos objectes Vectors. Si volem restar o sumar cooredenades s'haura de jugar amb el signe del Vectors que operam. Molt util per quan volguem que es mogui Bender
    public Vector opera(Vector v) {
        return new Vector(this.getX() + v.getX(), this.getY() + v.getY());
    }

    //Metode que ens permet saber quina es la distancia entre dos Vectors. Aquesta distancia ho calcularem gracies a càlcul de Euclides. Molt util per calcular distancia entre diferents teleports
    public double distanciaEuclidiana(Vector v) {
        return Math.sqrt(Math.pow((v.x - this.x), 2) + Math.pow((v.y - this.y), 2));
    }

    //Un simple toString que ens retorna les coordenades X i Y juntes.
    @Override
    public String toString() {
        return x + "," + y;
    }

    //Un metode equals que fa override de Object per comprovar si dos Vectors son iguals.
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector) {
            Vector v = (Vector) obj;
            if (this.x == v.x && this.y == v.y) return true;
        }
        return false;
    }

    //Getters and Setters
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

//Classe Estat només utilitzada per el calcular quina es la distancia més rapida a arribar (bestRun). Molt parescut a la classe Vector però en aquesta classe introduim l'atribut distancia, necessari pel
//metode mencionat.
class Estat {
    private int x;
    private int y;
    private int d;

    //Al constructor li passem com a parametre la fila, la columna i la distancia
    public Estat(int x, int y, int d) {
        this.x = x;
        this.y = y;
        this.d = d;
    }

    //Getters and Setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }
}