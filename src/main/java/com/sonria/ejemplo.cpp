#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <sstream>
#include <algorithm>
#include <filesystem>

namespace fs = std::filesystem;

const std::string UNIFIED = "unified.csv";
const std::string RESULT = "result.csv";
const std::vector<std::string> TITULOS = {
    "ID CONTRATO PROFESIONAL IS", "HONORARIO", "FECHA HONORARIO", "ID PACIENTE", "PLAN TARIFARIO",
    "PROCEDIMIENTO", "NOMBRE PROCEDIMIENTO", "SUBPROCEDIMIENTO", "NOMBRE SUBPROCEDIMIENTO",
    "ZONA", "UBICACIÓN", "VALOR EVOLUCION", "ESPECIALIDAD PROCEDIMIENTO", "FORMA PAGO",
    "ID PROFESIONAL", "CENTRO DE COSTO", "NIT", "CONCEPTO", "% HONORARIO", "VALOR CONTRATADO",
    "id honorario", "numero  contrato", "fecha  factura evolucion", "TIPO HONORARIO", "CLINICA",
    "ESPECIALIDAD PROFESIONAL", "NOMBRE PROFESIONAL", "CLASE NOMINA", "CONCEPTO HOMOLOGADO", "FECHA DEV"
};

void processFiles() {
    std::vector<std::tuple<std::string, std::string, bool>> archivos = {
        {"GARD_Nuevos.csv", "GARANTIAS NEGATIVAS", true},
        {"HNE_Nuevos.csv", "HONORARIOS NEGATIVOS", true},
        {"HON_Nuevos.csv", "HONORARIOS POSITIVOS", false},
        {"GARP_Nuevos.csv", "GARANTIAS POSITIVAS", false}
    };

    std::vector<std::string> allData;
    for (const auto& archivo : archivos) {
        std::ifstream file(std::get<0>(archivo));
        if (!file.is_open()) {
            std::cerr << "Error al leer el archivo: " << std::get<0>(archivo) << std::endl;
            continue;
        }

        std::string line;
        while (std::getline(file, line)) {
            std::vector<std::string> valueList;
            std::stringstream ss(line);
            std::string item;
            while (std::getline(ss, item, ';')) {
                valueList.push_back(item);
            }

            if (std::get<2>(archivo)) {
                std::string value18 = valueList.size() > 18 ? valueList[18] : "N/A";
                valueList.push_back(std::get<1>(archivo));
                valueList.push_back(value18);
            } else {
                valueList.push_back(std::get<1>(archivo));
                valueList.push_back("_");
            }

            std::string newLine;
            for (size_t i = 0; i < valueList.size(); ++i) {
                newLine += valueList[i];
                if (i < valueList.size() - 1) newLine += ";";
            }
            allData.push_back(newLine);
        }
        file.close();
    }

    std::ofstream unifiedFile(UNIFIED);
    if (!unifiedFile.is_open()) {
        std::cerr << "Error al crear el archivo unificado." << std::endl;
        return;
    }

    for (const auto& line : allData) {
        unifiedFile << line << "\r\n";
    }
    unifiedFile.close();
}

void insertColumns() {
    std::ifstream unifiedFile(UNIFIED);
    if (!unifiedFile.is_open()) {
        std::cerr << "Error al abrir el archivo unificado." << std::endl;
        return;
    }

    std::vector<std::string> allData;
    std::string line;
    while (std::getline(unifiedFile, line)) {
        std::vector<std::string> valueList;
        std::stringstream ss(line);
        std::string item;
        while (std::getline(ss, item, ';')) {
            valueList.push_back(item);
        }

        valueList.insert(valueList.begin() + 6, "");
        valueList.insert(valueList.begin() + 8, "");
        for (int i = 24; i <= 28; ++i) {
            valueList.insert(valueList.begin() + i, "a" + std::to_string(i - 23));
        }

        std::string newLine;
        for (size_t i = 0; i < valueList.size(); ++i) {
            newLine += valueList[i];
            if (i < valueList.size() - 1) newLine += ";";
        }
        allData.push_back(newLine);
    }
    unifiedFile.close();

    std::ofstream resultFile(RESULT);
    if (!resultFile.is_open()) {
        std::cerr << "Error al crear el archivo resultante." << std::endl;
        return;
    }

    std::string header;
    for (size_t i = 0; i < TITULOS.size(); ++i) {
        header += TITULOS[i];
        if (i < TITULOS.size() - 1) header += ";";
    }
    resultFile << header << "\r\n";

    for (const auto& line : allData) {
        resultFile << line << "\r\n";
    }
    resultFile.close();

    fs::remove(UNIFIED);
}

void exe1() {
    processFiles();
    insertColumns();
}

int main() {
    exe1();
    return 0;
}