CREATE TABLE dados_meteorologicos (
    id SERIAL PRIMARY KEY,
    cidade VARCHAR(255) NOT NULL,
    data_registro DATE NOT NULL,
    tempo_dia VARCHAR(50) NOT NULL,
    tempo_noite VARCHAR(50) NOT NULL,
    temperatura_max INTEGER NOT NULL,
    temperatura_min INTEGER NOT NULL,
    precipitacao INTEGER NOT NULL,
    umidade INTEGER NOT NULL,
    velocidade_vento INTEGER NOT NULL
);