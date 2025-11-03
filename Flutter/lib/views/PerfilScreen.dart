import 'dart:async';
import 'package:flutter/material.dart';
import 'package:hello_world/services/PerfilService.dart';
import 'package:hello_world/services/UsuarioService.dart'; 

class Perfilscreen extends StatefulWidget {
  static String routeName = '/perfil-screen';

  const Perfilscreen({Key? key}) : super(key: key);

  @override
  _PerfilScreen createState() => _PerfilScreen();
}

class _PerfilScreen extends State<Perfilscreen> {
  final Perfilservice _perfilService = Perfilservice();
  final Usuarioservice _usuarioservice = Usuarioservice();

  late TextEditingController _nombresController;
  late TextEditingController _apellidosController;
  late TextEditingController _telefonoController;

  bool _isLoading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    // inicializamos controladores vacíos; los llenaremos cuando carguen los datos
    _nombresController = TextEditingController();
    _apellidosController = TextEditingController();
    _telefonoController = TextEditingController();
    _loadUserData();
  }

  @override
  void dispose() {
    _nombresController.dispose();
    _apellidosController.dispose();
    _telefonoController.dispose();
    super.dispose();
  }

  Future<void> _loadUserData() async {
    setState(() {
      _isLoading = true;
      _error = null;
    });

    try {
      final data = await  _usuarioservice.getDataUsuario()
          .timeout(const Duration(seconds: 15));

      // Asumimos claves en minúscula tal y como definiste en el DTO
      _nombresController.text = data['nombres'] ?? '';
      _apellidosController.text = data['apellidos'] ?? '';
      _telefonoController.text = data['telefono'] ?? '';

      setState(() {
        _isLoading = false;
      });
    } on TimeoutException {
      setState(() {
        _isLoading = false;
        _error = 'Tiempo de espera agotado al cargar datos (15s). Intenta de nuevo.';
      });
    } catch (e) {
      setState(() {
        _isLoading = false;
        _error = 'Error al cargar los datos del usuario.';
      });
    }
  }

  Future<void> _saveProfile() async {
    final nombres = _nombresController.text.trim();
    final apellidos = _apellidosController.text.trim();
    final telefono = _telefonoController.text.trim();

    // validaciones simples
    if (nombres.isEmpty || apellidos.isEmpty || telefono.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Nombre, apellidos y telefono son obligatorios')),
      );
      return;
    }

    setState(() => _isLoading = true);

    final success = await _perfilService.updateUsuario(
      nombres: nombres,
      apellidos: apellidos,
      telefono: telefono,
    );

    setState(() => _isLoading = false);

    if (success) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Perfil actualizado correctamente')),
      );
      Navigator.pop(context, true); // ✅ devolvemos true para avisar que se actualizó
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Error actualizando perfil')),
      );
    }
  }

  Future<void> _showUpdatePasswordDialog() async {
    final _newPassController = TextEditingController();
    final _confirmPassController = TextEditingController();
    final _formKey = GlobalKey<FormState>();
    bool _submitting = false;

    await showDialog(
      context: context,
      builder: (context) {
        return StatefulBuilder(builder: (context, setStateDialog) {
          return AlertDialog(
            title: const Text('Actualizar contraseña'),
            content: Form(
              key: _formKey,
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  // Nota: no mostramos ni precargamos la contraseña hashed
                  TextFormField(
                    controller: _newPassController,
                    decoration: const InputDecoration(
                      labelText: 'Nueva contraseña',
                    ),
                    obscureText: true,
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return 'Ingresa la nueva contraseña';
                      }
                      if (value.length < 6) {
                        return 'Mínimo 6 caracteres';
                      }
                      return null;
                    },
                  ),
                  TextFormField(
                    controller: _confirmPassController,
                    decoration: const InputDecoration(
                      labelText: 'Confirmar contraseña',
                    ),
                    obscureText: true,
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return 'Confirma la contraseña';
                      }
                      if (value != _newPassController.text) {
                        return 'Las contraseñas no coinciden';
                      }
                      return null;
                    },
                  ),
                ],
              ),
            ),
            actions: [
              TextButton(
                onPressed: _submitting ? null : () => Navigator.of(context).pop(),
                child: const Text('Cancelar'),
              ),
              ElevatedButton(
                onPressed: _submitting
                    ? null
                    : () async {
                        if (!_formKey.currentState!.validate()) return;

                        setStateDialog(() => _submitting = true);

                        final success = await _perfilService.updatePassword(
                          password: _newPassController.text,
                        );

                        setStateDialog(() => _submitting = false);

                        Navigator.of(context).pop();

                        if (success) {
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(content: Text('Contraseña actualizada correctamente')),
                          );
                        } else {
                          ScaffoldMessenger.of(context).showSnackBar(
                            const SnackBar(content: Text('Error actualizando contraseña')),
                          );
                        }
                      },
                child: _submitting
                    ? const SizedBox(
                        width: 16, height: 16, child: CircularProgressIndicator(strokeWidth: 2))
                    : const Text('Actualizar'),
              ),
            ],
          );
        });
      },
    );

    _newPassController.dispose();
    _confirmPassController.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Mi Perfil'),
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _error != null
              ? Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        _error!,
                        textAlign: TextAlign.center,
                        style: const TextStyle(color: Colors.red),
                      ),
                      const SizedBox(height: 12),
                      ElevatedButton.icon(
                        icon: const Icon(Icons.refresh),
                        label: const Text('Reintentar'),
                        onPressed: _loadUserData,
                      )
                    ],
                  ),
                )
              : SingleChildScrollView(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    children: [
                      TextFormField(
                        controller: _nombresController,
                        decoration: const InputDecoration(
                          labelText: 'Nombres',
                          prefixIcon: Icon(Icons.person),
                        ),
                      ),
                      const SizedBox(height: 12),
                      TextFormField(
                        controller: _apellidosController,
                        decoration: const InputDecoration(
                          labelText: 'Apellidos',
                          prefixIcon: Icon(Icons.person_outline),
                        ),
                      ),
                      const SizedBox(height: 12),
                      TextFormField(
                        controller: _telefonoController,
                        decoration: const InputDecoration(
                          labelText: 'Teléfono',
                          prefixIcon: Icon(Icons.phone),
                        ),
                        keyboardType: TextInputType.phone,
                      ),
                      const SizedBox(height: 20),
                      Row(
                        children: [
                          Expanded(
                            child: ElevatedButton.icon(
                              icon: const Icon(Icons.save),
                              label: const Text('Guardar cambios'),
                              onPressed: _saveProfile,
                            ),
                          ),
                          const SizedBox(width: 12),
                          ElevatedButton.icon(
                            icon: const Icon(Icons.lock),
                            label: const Text('Actualizar contraseña'),
                            style: ElevatedButton.styleFrom(
                              backgroundColor: Colors.grey[700],
                            ),
                            onPressed: _showUpdatePasswordDialog,
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
    );
  }
}