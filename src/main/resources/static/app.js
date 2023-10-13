 angular.module('myApp', [])
        .controller('HierarchyController', ['$scope','$http', function($scope, $http) {
            $scope.employees = []
            
            // Função para carregar dados da API
            $scope.loadEmployees = function() {
                $http.get('http://localhost:8080/colaboradores')
                    .then(function(response) {
                        $scope.employees = response.data;
                    })
                    .catch(function(error) {
                        alert('Erro ao carregar colaboradores da API:', error);
                    });
            };

            $scope.newEmployee = {};
            $scope.newEmployee.nivel = 1;
            $scope.editing = false; 
            $scope.editedEmployee = {}; 
            var idEditing;

            $scope.addEmployee = function() {


                var newEmployee = {
                    nome: $scope.newEmployee.nome,
                    nivel: $scope.newEmployee.nivel || 1,
                    senha: $scope.newEmployee.senha
                };


                $http.post('http://localhost:8080/colaboradores', newEmployee)
                    .then(function(response) {
                        $scope.loadEmployees(); // Atualiza a tabela
                    })
                    .catch(function(error) {
                        alert('Erro ao criar um novo colaborador:', error);
                    });

                $scope.newEmployee = {}; // Limpar o formulário
                $scope.newEmployee.nivel = 1;
            };

            $scope.editEmployee = function(employee) {
                idEditing = employee.id;
                $scope.editedEmployee = angular.copy(employee);
                $scope.editedEmployee.senha = "";
                $scope.editing = true;
            };

            $scope.saveEditedEmployee = function() {

                var editedEmployee = {
                    nome: $scope.editedEmployee.nome,
                    nivel: $scope.editedEmployee.nivel || 1,
                    senha: $scope.editedEmployee.senha
                };


                $http.put('http://localhost:8080/colaboradores/' + editedEmployee.id, editedEmployee)
                    .then(function(response) {
                        $scope.loadEmployees(); // Atualiza a tabela
                    })
                    .catch(function(error) {
                        alert('Erro ao atualizar o colaborador:', error);
                    });

                $scope.editedEmployee = {}; // Limpar o formulário
                
                $scope.editing = false; // Fecha o formulário de edição
            };

            $scope.cancelEdit = function() {
                $scope.editing = false; // Cancela a edição e fecha o formulário de edição
            };

             $scope.deleteEmployee = function(employee) {
                 $http.delete('http://localhost:8080/colaboradores/'+ employee.id)
                    .then(function(response) {
                        $scope.loadEmployees(); // Atualiza a tabela
                    })
                    .catch(function(error) {
                        alert('Erro ao deletar o colaborador:', error);
                    });
            };

            $scope.loadEmployees();
        }]);