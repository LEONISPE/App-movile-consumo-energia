
import 'package:App/services/UsuarioService.dart';
import 'package:flutter/material.dart';

class EmailMiembroScreen extends StatefulWidget {
  static String routeName = '/email-miembro-screen';

  const EmailMiembroScreen({super.key});

  @override
  State<EmailMiembroScreen> createState() => _EmailMiembroScreenState();
}

class _EmailMiembroScreenState extends State<EmailMiembroScreen> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _emailController = TextEditingController();

  final Usuarioservice _usuarioService = Usuarioservice();
  String? _errorMessage;
  bool _loading = false;

  @override
  Widget build(BuildContext context) {
    final miembro = ModalRoute.of(context)!.settings.arguments as Map<String, dynamic>;
    final int miembroId = miembro["id"]; // <-- AQUÍ SE RECIBE EL ID

    return Scaffold(
      appBar: AppBar(title: const Text("Asignar Email al Miembro")),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              const Text(
                "Ingrese el correo electrónico que este miembro usará para iniciar sesión.",
                style: TextStyle(fontSize: 17),
              ),
              const SizedBox(height: 10),
              Container(
                padding: const EdgeInsets.all(10),
                decoration: BoxDecoration(
                  color: Colors.yellow.shade100,
                  borderRadius: BorderRadius.circular(10),
                ),
                child: const Text(
                  "⚠️ Tenga en cuenta que este será el email del miembro.\n"
                  "La contraseña deberá establecerse desde la pantalla de inicio de sesión → opción 'Soy miembro - Establecer contraseña'.",
                  style: TextStyle(fontSize: 14),
                ),
              ),
              const SizedBox(height: 25),

              TextFormField(
                controller: _emailController,
                decoration: InputDecoration(
                  labelText: "Nuevo email del miembro",
                  border: OutlineInputBorder(borderRadius: BorderRadius.circular(10)),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) return "Ingrese un email";
                  final regex = RegExp(r'^[^@]+@[^@]+\.[^@]+');
                  if (!regex.hasMatch(value)) return "Email inválido";
                  return null;
                },
              ),

              const SizedBox(height: 25),

              _loading
                  ? const CircularProgressIndicator()
                  : SizedBox(
                      width: double.infinity,
                      child: ElevatedButton(
                        onPressed: () async {
                          if (!_formKey.currentState!.validate()) return;

                          setState(() => _loading = true);

                          final email = _emailController.text.trim();
                          final ok = await _usuarioService.setearEmailMiembro(email, miembroId);

                          setState(() => _loading = false);

                          if (ok) {
                            Navigator.pop(context);
                            ScaffoldMessenger.of(context).showSnackBar(
                              const SnackBar(content: Text("Email asignado correctamente ✅")),
                            );
                             Navigator.pushReplacementNamed(context, '/dashboard');
                          } else {
                            setState(() {
                              _errorMessage = "Error al asignar el email";
                            });
                          }
                        },
                        child: const Text("Asignar Email"),
                      ),
                    ),

              if (_errorMessage != null)
                Padding(
                  padding: const EdgeInsets.only(top: 15),
                  child: Text(
                    _errorMessage!,
                    style: const TextStyle(color: Colors.red),
                  ),
                ),
            ],
          ),
        ),
      ),
    );
  }
}
