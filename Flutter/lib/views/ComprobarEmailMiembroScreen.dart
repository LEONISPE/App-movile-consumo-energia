import 'package:App/services/UsuarioService.dart';
import 'package:flutter/material.dart';

class Comprobaremailmiembroscreen extends StatefulWidget {
  static String routeName = '/password-miembro-screen';
  const Comprobaremailmiembroscreen({super.key});

  @override
  State<Comprobaremailmiembroscreen> createState() =>
      _PasswordmiembroscreenState();
}

class _PasswordmiembroscreenState extends State<Comprobaremailmiembroscreen> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  bool _passwordVisible = false;

  final Usuarioservice _usuarioService = Usuarioservice();
  String? _errorMessage;
  bool _loading = false;

  bool _modoSetPassword = false;
  late String _emailMiembro;

  Future<void> _comprobarEmail() async {
    if (!_formKey.currentState!.validate()) return;

    final email = _emailController.text.trim();
    try {
      final ok = await _usuarioService.comprobarEmailMiembro(email);
      if (ok) {
        setState(() {
          _modoSetPassword = true;
          _emailMiembro = email;
        });
      } else {
        setState(() => _errorMessage = "Email incorrecto o no registrado");
      }
    } catch (e) {
      setState(() => _errorMessage = "Error de conexión");
    }
  }

  Future<void> _setearPassword() async {
    if (!_formKey.currentState!.validate()) return;
    final password = _passwordController.text.trim();

    try {
      final ok = await _usuarioService.setearPasswordMiembro(
        _emailMiembro,
        password,
      );
      if (ok) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("Contraseña actualizada ✅")),
        );
        Navigator.pushReplacementNamed(context, '/login-screen');
      }
    } catch (e) {
      setState(() => _errorMessage = "Error al guardar contraseña");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Email  Miembro")),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              if (!_modoSetPassword) ...[
                // MODO COMPROBAR EMAIL
                const Text("Ingrese el correo electrónico del miembro."),
                const SizedBox(height: 20),

                TextFormField(
                  controller: _emailController,
                  decoration: InputDecoration(
                    labelText: "Email",
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(10),
                    ),
                  ),
                  validator: (value) => (value == null || value.isEmpty)
                      ? "Ingrese un email"
                      : null,
                ),

                const SizedBox(height: 20),

                ElevatedButton(
                  onPressed: _comprobarEmail,
                  child: const Text("Validar email"),
                ),
              ],

              if (_modoSetPassword) ...[
                // MODO SETEAR PASSWORD
                const Text("Ingrese la nueva contraseña."),
                const SizedBox(height: 20),

                TextFormField(
                  controller: _passwordController,
                  obscureText: !_passwordVisible,
                  decoration: InputDecoration(
                    labelText: "Nueva contraseña",
                    suffixIcon: IconButton(
                      icon: Icon(
                        _passwordVisible
                            ? Icons.visibility
                            : Icons.visibility_off,
                      ),
                      onPressed: () {
                        setState(() => _passwordVisible = !_passwordVisible);
                      },
                    ),
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(10),
                    ),
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty)
                      return "Ingrese una contraseña";
                    if (!RegExp(
                      r'^(?=.*[A-Z])(?=.*\d).{8,}$',
                    ).hasMatch(value)) {
                      return "Debe tener 8 caracteres, 1 número y 1 mayúscula";
                    }
                    return null;
                  },
                ),

                const SizedBox(height: 20),

                ElevatedButton(
                  onPressed: _setearPassword,
                  child: const Text("Guardar contraseña"),
                ),
              ],

              if (_errorMessage != null)
                Text(_errorMessage!, style: const TextStyle(color: Colors.red)),
            ],
          ),
        ),
      ),
    );
  }
}
