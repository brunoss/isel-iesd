![deadlock](https://user-images.githubusercontent.com/1324339/114273325-7ef46700-9a11-11eb-8123-622f03bef221.PNG)

Um deadlock ocorre quando os recursos são adquiridos por ordem diferente.

1. T1 adquire recurso 1
2. T2 aquire recurso 2
3. T1 tenta adquirir recurso 2, mas já está ocupado por T2 e bloqueia
4. T2 tenta adquirir recurso 1, mas já está ocupado por T1 e bloqueia

Ambas as threads ficam bloqueadas e os recursos ocupados. 
Qualquer outra thread que tente adquirir um dos recursos ficará também ocupada.