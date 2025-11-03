import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:hello_world/helpers/router.dart';
import 'package:hello_world/views/LoginScreen.dart';


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
      builder: (context, child) {
        return MaterialApp(
          debugShowCheckedModeBanner: false,
          title: 'mi app de flutter',
          theme: ThemeData(primarySwatch: Colors.blue),
          initialRoute: LoginScreen.routeName,
          routes: routes,
          home: child,
        );
      },
      child: const LoginScreen(),
    );
  }
}