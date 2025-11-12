import 'package:App/services/UsuarioService.dart';
import 'package:flutter/material.dart';


class MiembrosScreen extends StatefulWidget {
    static String routeName = '/miembros-screen';
  const MiembrosScreen({Key? key}) : super(key: key);

  @override
  State<MiembrosScreen> createState() => _MiembrosScreenState();
}

class _MiembrosScreenState extends State<MiembrosScreen> {

  final Usuarioservice  _usuarioService = Usuarioservice();
  bool _isLoading = true;
  String? _error;
  String? _userRole;
  List<dynamic> _miembros = [];

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  Future<void> _loadData() async {
  try {
    final data = await _usuarioService.getDataMiembros();

    // Obtener el rol del usuario
    final role = await _usuarioService.getUserRole();

    setState(() {
      _miembros = data;
      _userRole = role ?? 'MIEMBRO';
      _isLoading = false;
    });
  } catch (e) {
    setState(() {
      _error = "Error al cargar los miembros: $e";
      _isLoading = false;
    });
  }
}

  void _showSubmenu(BuildContext context, dynamic miembro) {
  showModalBottomSheet(
    context: context,
    builder: (context) {
      return Wrap(
        children: [
          ListTile(
            leading: const Icon(Icons.check_circle_outline, color: Colors.green),
            title: const Text('Autorizar'),
            subtitle: const Text("Permitir que este miembro acceda al hogar"),
            onTap: () {
              Navigator.pop(context);
              Navigator.pushNamed(
                context,
                '/email-miembro-screen',
                arguments: miembro,
              );
            },
          ),
          ListTile(
            leading: const Icon(Icons.cancel_outlined, color: Colors.red),
            title: const Text('Cancelar'),
            onTap: () => Navigator.pop(context),
          ),
        ],
      );
    },
  );
}

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Miembros del Hogar")),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _error != null
              ? Center(child: Text(_error!))
              : _miembros.isEmpty
                  ? const Center(child: Text("No hay miembros registrados"))
                  : ListView.builder(
                      itemCount: _miembros.length,
                      itemBuilder: (context, index) {
                        final miembro = _miembros[index];
                        return Card(
                          margin: const EdgeInsets.all(6),
                          elevation: 4,
                          child: ListTile(
                            leading: const Icon(Icons.person_outline),
                            title: Text(
                              "${miembro['nombre'] ?? 'Desconocido'} ${miembro['apellidos'] ?? ''}",
                              style: const TextStyle(
                                fontSize: 18,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                            subtitle: Text(
                              "Email: ${miembro['email'] ?? 'N/A'}\nRole: ${miembro['role'] ?? 'N/A'}",
                            ),
                            trailing: const Icon(Icons.arrow_forward_ios, size: 18),
                            onTap: _userRole == 'DUEÑO'
                                ? () => _showSubmenu(context, miembro)
                                : null, // los miembros no pueden abrir el menú
                          ),
                        );
                      },
                    ),
    );
  }
}