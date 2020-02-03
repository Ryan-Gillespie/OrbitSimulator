
public enum Planet {
  //Name  ( a,     e,     mass,       i,         phi)
  Mercury ( 0.387, 0.206,    0.0553,  7.00487,    77.45645, "Sun"),
  Venus   ( 0.723, 0.0068,   0.815,   3.39471,   131.53298, "Sun"),
  Earth   ( 1.00,  0.0167,   1.00,    0.0005,    102.94719, "Sun"),
  Mars    ( 1.524, 0.0934,   0.1074,  1.85061,   336.04084, "Sun"),
  Jupiter ( 5.20,  0.0485, 318.0,     1.30530,    14.75385, "Sun"),
  Saturn  ( 9.54,  0.0555,  95.0,     2.48446,    92.43194, "Sun"),
  Uranus  (19.19,  0.0469,  14.52,    0.76986,   170.96424, "Sun"),
  Neptune (30.1,   0.009,   17.15,    1.76917,    44.97135, "Sun"),
  Pluto   (39.5,   0.249,    0.0025, 17.14175,   224.06676, "Sun"),
  //Name    (a,          e,       mass,     i,      phi,     parent)
  Moon      (0.0025,     0.0549,  0.0123,   5.145,  125.08,  "Earth"),
  Phobos    (6.27014e-4, 0.0151,  2.e-9,    1.075,  164.931, "Mars"),
  Deimos    (0.0016,     0.0002,  2.48e-10, 1.793,  339.6,   "Mars"),
  Io        (0.000281,   0.0041,  0.0149,   0.036,  43.977,  "Jupiter"),
  Europa    (0.000448,   0.0094,  0.008,    0.466, 219.006,  "Jupiter"),
  Ganymede  (0.00716,    0.0013,  2.477e-2, 0.177,  63.552,  "Jupiter"),
  Callisto  (0.0126,     0.0074,  1.807e-2, 0.192, 298.848,  "Jupiter"),
  Mimas     (0.00124,    0.0196,  6.35e-6,  1.572, 153.152,  "Jupiter"),
  Enceladus (0.00159,    0.0047,  1.8e-5,   0.009,  93.204,  "Jupiter");/*
  Tethys    (6),
  Dione     (6),
  Rhea      (6),
  Titan     (6),
  Hyperion  (6),
  Iapetus   (6),
  Pheobe    (6),
  Miranda   (7),
  Ariel     (7),
  Umbriel   (7),
  Titania   (7),
  Oberon    (7),
  Proteus   (8),
  Titron    (8),
  Nereid    (8),
  Charon    (9);*/
  
  public final double a;       // semi-major axis in Au
  public final double e;       // eccentricity
  public final double i;       // inclination in degrees
  public final double phi;     // longitude of periapsis in degrees
  public final double mass;    // mass in earth masses
  public final String parent;  // parent body
  
  public final double period;
  
  Planet(double a, double e, double mass, double i, double phi, String parent) {
    this.a = a;
    this.e = e;
    this.mass = mass;
    this.i = Math.toRadians(i);
    this.phi = Math.toRadians(phi);
    this.parent = parent;
    this.period = Math.pow(a, 1.5);
  }
}