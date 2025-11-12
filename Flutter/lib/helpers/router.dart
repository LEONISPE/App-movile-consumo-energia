
import 'package:App/views/ComprobarEmailMiembroScreen.dart';
import 'package:App/views/EmailMiembroScreen.dart';
import 'package:App/views/MiembrosScreen.dart';
import 'package:flutter/material.dart';
import '../views/AguaScreen.dart';
import '../views/DashboardScreen.dart';
import '../views/EnergiaScreen.dart';
import '../views/FacturaScreen.dart';
import '../views/GasScreen.dart';
import '../views/LoginScreen.dart';
import '../views/PerfilScreen.dart';

final Map<String, WidgetBuilder> routes = {
  LoginScreen.routeName: (context) => const LoginScreen(),
  DashboardScreen.routeName: (context) => const DashboardScreen(),
  AguaScreen.routeName: (context) => const AguaScreen(),
  Energiascreen.routeName: (context) => const Energiascreen(),
  Gasscreen.routeName: (context) => const Gasscreen(),
  FacturaScreen.routeName: (context) => const FacturaScreen(servicio: "AGUA", titulo: "Facturas"), 
  Perfilscreen.routeName : (context) => const Perfilscreen(),
  MiembrosScreen.routeName : (context) => const MiembrosScreen(),
  EmailMiembroScreen.routeName : (context) => const EmailMiembroScreen(),
  Comprobaremailmiembroscreen.routeName : (context) => const Comprobaremailmiembroscreen()
};