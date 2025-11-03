
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:hello_world/services/LoginService.dart';
import 'package:http/http.dart' as http;

class Perfilservice {

/// Actualiza los datos del usuario (sin contraseña)
  Future<bool> updateUsuario({
    required String nombres,
    required String apellidos,
    required String telefono,
  }) async {
    final url = Uri.parse('http://localhost:8080/user/update/usuario');
    final token = LoginService.token;

    final body = jsonEncode({
      'nombres': nombres,
      'apellidos': apellidos,
      'telefono': telefono,
    });

    final response = await http.put(
      url,
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer $token",
      },
      body: body,
    );

    if (response.statusCode == 200 || response.statusCode == 204) {
      return true;
    } else {
      debugPrint('updateUsuario error: ${response.statusCode} ${response.body}');
      return false;
    }
  }
Future<bool> updatePassword({
  required String password,
}) async {
  final url = Uri.parse('http://localhost:8080/user/update/password');
  final token = LoginService.token;

  final body = jsonEncode({
    'password': password,
  });

  final response = await http.put(
    url,
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer $token",
    },
    body: body,
  );

  if (response.statusCode == 200 || response.statusCode == 204) {
    return true;
  } else {
    debugPrint('updatePassword error: ${response.statusCode} ${response.body}');
    return false;
  }
}
}



