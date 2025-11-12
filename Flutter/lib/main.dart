import 'package:App/helpers/router.dart';
import 'package:App/views/LoginScreen.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';



void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return ScreenUtilInit(
      designSize: const Size(390, 844),
      minTextAdapt: true,
      splitScreenMode: true,
      builder: (_, __) {
        return MaterialApp(
          debugShowCheckedModeBanner: false,
          title: 'mi app de flutter',
          theme: ThemeData(primarySwatch: Colors.blue),
          initialRoute: LoginScreen.routeName, 
          routes: routes, 
        );
      },
    );
  }
}