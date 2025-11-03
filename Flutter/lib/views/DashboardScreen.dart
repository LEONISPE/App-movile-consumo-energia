import 'dart:async';
import 'package:flutter/material.dart';
import 'package:animate_do/animate_do.dart';
import 'package:hello_world/services/LoginService.dart';
import 'package:hello_world/services/UsuarioService.dart';

class DashboardScreen extends StatefulWidget {
  static String routeName = '/dashboard';
  final bool modoFacturas; 

  const DashboardScreen({Key? key, this.modoFacturas = false})
    : super(key: key);

  @override
  State<DashboardScreen> createState() => _DashboardScreenState();
}

class _DashboardScreenState extends State<DashboardScreen> {
  final Usuarioservice _usuarioservice = Usuarioservice();

  String nombres = "";
  String apellidos = "";
  String telefono = "";
  String email = "";

  bool _isLoading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadUsuarioData();
  }

  Future<void> _loadUsuarioData() async {
    try {
      final response = await _usuarioservice.getDataUsuario().timeout(
        const Duration(seconds: 15),
      );
      final data = response;
    

      setState(() {
        nombres = data['nombres'] ?? "";
        apellidos = data['apellidos'] ?? "";
        telefono = data['telefono'] ?? "";
        email = data['email'] ?? "";
        _isLoading = false;
      });
    } on TimeoutException {
      setState(() {
        _error =
            '⏰ Tiempo de espera agotado AWS otraves esta caido  (15s). Por favor, reintente.';
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _error = 'Error al cargar los datos  del usuario';
        _isLoading = false;
      });
    }
  }

  int selectedService = -1;

  final List<Map<String, dynamic>> services = [
    {"name": "Agua", "icon": Icons.water_drop, "color": Colors.blueAccent},
    {"name": "Energia", "icon": Icons.lightbulb_outline, "color": Colors.amber},
    {
      "name": "Gas",
      "icon": Icons.local_fire_department,
      "color": Colors.orangeAccent,
    },
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: Text(
          widget.modoFacturas
              ? 'Selecciona un servicio para ver su factura'
              : 'Selecciona un servicio',
        ),
        centerTitle: true,
        backgroundColor: Colors.blueAccent,
      ),
      drawer: _drawerWidget(context),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 30.0, vertical: 20.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              FadeInDown(
                child: Text(
                  widget.modoFacturas
                      ? '¿De qué servicio deseas ver tus facturas?'
                      : '¿Qué servicio deseas ver?',
                  style: const TextStyle(
                    fontSize: 26,
                    fontWeight: FontWeight.bold,
                    color: Colors.black87,
                  ),
                  textAlign: TextAlign.center,
                ),
              ),
              const SizedBox(height: 40),
              // Lista vertical de servicios
              ...List.generate(services.length, (index) {
                final service = services[index];
                final isSelected = selectedService == index;

                return FadeInUp(
                  delay: Duration(milliseconds: 200 * index),
                  child: GestureDetector(
                    onTap: () {
                      setState(() {
                        selectedService = index;
                      });

                      final String serviceName = service['name'];

                      if (widget.modoFacturas) {
                        if (serviceName == 'Agua') {
                          Navigator.pushNamed(context, '/factura-agua-screen');
                        } else if (serviceName == 'Energia') {
                          Navigator.pushNamed(
                            context,
                            '/factura-energia-screen',
                          );
                        } else if (serviceName == 'Gas') {
                          Navigator.pushNamed(context, '/factura-gas-screen');
                        }
                      } else {
                        if (serviceName == 'Agua') {
                          Navigator.pushNamed(context, '/agua-screen');
                        } else if (serviceName == 'Energia') {
                          Navigator.pushNamed(context, '/energia-screen');
                        } else if (serviceName == 'Gas') {
                          Navigator.pushNamed(context, '/gas-screen');
                        }
                      }
                    },
                    child: AnimatedContainer(
                      duration: const Duration(milliseconds: 300),
                      margin: const EdgeInsets.symmetric(vertical: 10),
                      padding: const EdgeInsets.symmetric(
                        vertical: 20,
                        horizontal: 25,
                      ),
                      decoration: BoxDecoration(
                        color: isSelected
                            ? service['color'].withOpacity(0.1)
                            : Colors.grey.shade100,
                        borderRadius: BorderRadius.circular(20),
                        border: Border.all(
                          color: isSelected
                              ? service['color']
                              : Colors.transparent,
                          width: 2,
                        ),
                        boxShadow: [
                          BoxShadow(
                            color: Colors.black.withOpacity(0.05),
                            blurRadius: 6,
                            offset: const Offset(0, 3),
                          ),
                        ],
                      ),
                      child: Row(
                        children: [
                          CircleAvatar(
                            radius: 28,
                            backgroundColor: service['color'],
                            child: Icon(
                              service['icon'],
                              color: Colors.white,
                              size: 30,
                            ),
                          ),
                          const SizedBox(width: 20),
                          Expanded(
                            child: Text(
                              service['name'],
                              style: TextStyle(
                                fontSize: 22,
                                fontWeight: FontWeight.w600,
                                color: isSelected
                                    ? service['color']
                                    : Colors.black87,
                              ),
                            ),
                          ),
                          Icon(
                            Icons.arrow_forward_ios,
                            size: 18,
                            color: isSelected ? service['color'] : Colors.grey,
                          ),
                        ],
                      ),
                    ),
                  ),
                );
              }),
            ],
          ),
        ),
      ),
    );
  }

  Drawer _drawerWidget(BuildContext context) {
    return Drawer(
      child: _isLoading
          ? const Center(
              child: CircularProgressIndicator(color: Colors.blueAccent),
            )
          : _error != null
          ? Center(
              child: Padding(
                padding: const EdgeInsets.all(20),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text(
                      _error!,
                      textAlign: TextAlign.center,
                      style: const TextStyle(color: Colors.red, fontSize: 18),
                    ),
                    const SizedBox(height: 20),
                    ElevatedButton.icon(
                      onPressed: () {
                        _loadUsuarioData();
                      },
                      icon: const Icon(Icons.refresh),
                      label: const Text('Reintentar'),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: Colors.blueAccent,
                        foregroundColor: Colors.white,
                      ),
                    ),
                  ],
                ),
              ),
            )
          : SingleChildScrollView(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [_headerMethod(context), _menuDrawer(context)],
              ),
            ),
    );
  }

  Material _headerMethod(BuildContext context) {
    return Material(
      color: Colors.blueGrey,
      child: InkWell(
        child: Container(
          padding: EdgeInsets.only(
            top: MediaQuery.of(context).padding.top,
            bottom: 24,
          ),
          child: Column(
            children: [
              const CircleAvatar(
                radius: 70,
                backgroundImage: NetworkImage(
                  '/images/profile.jpg',
                ),
              ),
              const SizedBox(height: 12),
              Text(
                '$nombres $apellidos',
                style: const TextStyle(fontSize: 28, color: Colors.white),
              ),
              Text(
                '$email',
                style: TextStyle(fontSize: 14, color: Colors.white),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _menuDrawer(BuildContext context) {
    return Column(
      children: [
        ListTile(
          leading: const Icon(Icons.home_max_outlined),
          title: const Text("mi perfil"),
          onTap: ()async {
           final result = await Navigator.pushNamed(context, '/perfil-screen');
    if (result == true) {
      _loadUsuarioData();
    }
           },
        ),
        ListTile(
          leading: const Icon(Icons.favorite_border),
          title: const Text('Integrantes de Familia'),
          onTap: () {},
        ),
        ListTile(
          leading: const Icon(Icons.receipt_long),
          title: const Text('Historial de Facturas'),
          onTap: () {
            Navigator.pop(context); // Cierra el Drawer
            Navigator.push(
              context,
              MaterialPageRoute(
                builder: (context) => const DashboardScreen(modoFacturas: true),
              ),
            );
          },
        ),
        ListTile(
          leading: const Icon(Icons.logout, color: Colors.redAccent),
          title: const Text('Logout'),
          onTap: () => _confirmarLogout(context),
        ),
      ],
    );
  }

  void _confirmarLogout(BuildContext context) {
  showDialog(
    context: context,
    builder: (BuildContext ctx) {
      return AlertDialog(
        title: const Text('Cerrar sesión'),
        content: const Text('¿Está seguro de que desea cerrar sesión?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(ctx).pop(), // cancelar
            child: const Text('Cancelar'),
          ),
          TextButton(
            onPressed: () async {
              Navigator.of(ctx).pop(); // cerrar el diálogo

              await LoginService.logout(); // eliminar token

              // 🔹 Navegar al LoginScreen y limpiar historial
              Navigator.pushNamedAndRemoveUntil(
                context,
                '/login-screen',
                (route) => false,
              );
            },
            child: const Text('Cerrar sesión', style: TextStyle(color: Colors.redAccent)),
          ),
        ],
      );
    },
  );
}

}
