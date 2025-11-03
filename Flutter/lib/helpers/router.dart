
import 'package:flutter/material.dart';
import 'package:hello_world/views/AguaScreen.dart';
import 'package:hello_world/views/EnergiaScreen.dart';
import 'package:hello_world/views/Factura_agua_screen.dart';
import 'package:hello_world/views/GasScreen.dart';
import 'package:hello_world/views/LoginScreen.dart';
import 'package:hello_world/views/DashboardScreen.dart';
import 'package:hello_world/views/PerfilScreen.dart';


final Map<String, WidgetBuilder> routes = {
  LoginScreen.routeName: (context) => const LoginScreen(),
  DashboardScreen.routeName: (context) => const DashboardScreen(),
  AguaScreen.routeName: (context) => const AguaScreen(),
  Energiascreen.routeName: (context) => const Energiascreen(),
  Gasscreen.routeName: (context) => const Gasscreen(),
  FacturaAguaScreen.routeName : (context) => const FacturaAguaScreen(),
  Perfilscreen.routeName : (context) => const Perfilscreen()
  
};