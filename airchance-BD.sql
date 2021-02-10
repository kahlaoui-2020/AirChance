
drop table Clients cascade constraints;
drop table Hotesses cascade constraints;
drop table Modeles cascade constraints;
drop table Pilotes cascade constraints;
drop table Avions cascade constraints;
drop table Affecter cascade constraints;
drop table Effectue cascade constraints;
drop table Vols cascade constraints;
drop table Places cascade constraints;
drop table Qualifie cascade constraints;
drop table Reservations cascade constraints;


/*==============================================================*/
/* Table : CLIENTS                                              */
/*==============================================================*/
create table Clients
(
   numClient            number(5) not null,
   num_pass             varchar(20) not null,
   point_cumul          number(5) not null,
   nom                  varchar(20) not null,
   prenom               varchar(20) not null,
   numero               number(4) not null,
   rue                  varchar(15) not null,
   codepostale          number(5) not null,
   ville                varchar(20) not null,
   pays                 varchar(20) not null,
   constraint pk_clients primary key (numClient)
);
/*==============================================================*/
/* Table : HOTESSES                                             */
/*==============================================================*/
create table Hotesses
(
   numHotesse           number(5) not null,
   nbHeuresVol          number(5) not null,
   langues              varchar(50)not null,
   nom                  varchar(20) not null,
   prenom               varchar(20) not null,
   numero               number(4) not null,
   rue                  varchar(15) not null,
   codepostale          number(5) not null,
   ville                varchar(20) not null,
   pays                 varchar(20) not null,
   constraint PK_HOTESSES primary key (numHotesse)
);
/*==============================================================*/
/* Table : MODELES                                               */
/*==============================================================*/
create table ModelesAvion
(
   modele               varchar(20) not null,
   nb_equipe            number(2),
   ray_action           number(5),
   constraint pk_modeles primary key (modele)
);
/*==============================================================*/
/* Table : PILOTES                                              */
/*==============================================================*/
create table Pilotes
(
   numPilote            number(5) not null,
   nbHeuresvol          number(5) not null,
   nom                  varchar(20) not null,
   prenom               varchar(20) not null,
   numero               number(4) not null,
   rue                  varchar(15) not null,
   codepostale          number(5) not null,
   ville                varchar(20) not null,
   pays                 varchar(20) not null,
   constraint pk_pilotes primary key (numPilote)
);
/*==============================================================*/
/* Table : AVIONS                                              */
/*==============================================================*/
create table Avions
(
   numAvion             number(5) not null,
   modele               varchar(20) not null,
   nb_place_eco         number(3),
   nb_place_premiere    number(3),
   nb_place_affaire     number(3),
   constraint pk_avions primary key (numAvion),
	constraint fk_est foreign key (modele) references Modeles (modele)on delete cascade
);
/*==============================================================*/
/* Table : VOLS                                                  */
/*==============================================================*/
create table Vols
(
   numVol               number(5) not null,
   numAvion             number(5) not null,
   aero_dep             varchar(20) not null,
   aero_arrv            varchar(20) not null,
   horaire              date,
   date_dep             date,
   duree                varchar(6) not null,
   distance             number,
   min_nb_place_eco     number(3),
   min_nb_place_premiere number(3),
   min_nb_place_affaire number(3),
   constraint pk_vols primary key (numVol),
   constraint fk_associe foreign key (numAvion) references Avions (numAvion) on delete cascade
);
/*==============================================================*/
/* Table : AFFECTE                                              */
/*==============================================================*/
create table Affecte
(
   numAvion             number(5) not null,
   numHotesse           number(5) not null,
   numPilote            number(5) not null,
   constraint PK_affecte primary key (numAvion, NUMHOTESSE, numPilote),
	constraint FK_AFFECTE foreign key (numPilote) references Pilotes (numPilote) on delete cascade,
	constraint FK_AFFECTE2 foreign key (numAvion) references Avions (numAvion) on delete cascade,
	constraint FK_AFFECTE3 foreign key (numHotesse) references Hotesses (numHotesse) on delete cascade
);
/*==============================================================*/
/* Table : EFFECTUE                                             */
/*==============================================================*/
create table Effectue
(
   numVol               number(5) not null,
   numAvion				number(5) not null,
   numHotesse           number(5) not null,
   numPilote            number(5) not null,
   constraint PK_EFFECTUE primary key (numVol, numAvion, numHotesse, numPilote),
   constraint FK_EFFECTUE foreign key (numVol) references Vols (numVol) on delete cascade,
	constraint FK_EFFECTUE1 foreign key (numPilote) references Pilotes (numPilote) on delete cascade,
	constraint FK_EFFECTUE2 foreign key (numAvion) references Avions (numAvion) on delete cascade,
	constraint FK_EFFECTUE4 foreign key (numHotesse) references Hotesses (numHotesse) on delete cascade
);
/*==============================================================*/
/* Table : PLACES                                                */
/*==============================================================*/
create table Places
(
   numPlace             varchar(3) not null,
   numAvion             number(5) not null,
   position             varchar(10) not null,
   classe               varchar(10) not null,
   prix                 number(5) not null,
   constraint pk_places primary key (numPlace),
	constraint fk_contient foreign key (numAvion) references Avions (numAvion) on delete cascade,
	constraint places_position check (position in ('Hublot','Centre','Couloir')),
	constraint places_classe check (classe in ('Eco','Première','Affaire'))
);

/*==============================================================*/
/* Table : QUALIFIE                                             */
/*==============================================================*/
create table Qualifie
(
   MODELE               varchar(20) not null,
   NUMPILOTE            number(5) not null,
   constraint PK_QUALIFIE primary key (MODELE, NUMPILOTE),
	constraint FK_QUALIFIE foreign key (NUMPILOTE) references Pilotes (NUMPILOTE) on delete cascade,
	constraint FK_QUALIFIE2 foreign key (MODELE) references Modeles (MODELE) on delete cascade
);

/*==============================================================*/
/* Table : RESERVATIONS                                          */
/*==============================================================*/
create table Reservations
(
   numReservation       number(5) not null,
   numPlace             varchar(3) not null,
   numVol               number(5) not null,
   numClient            number(5) not null,
   date_res             date,
   prixTotal            number(10),
   constraint pk_reservations primary key (numReservation),
	constraint fk_concerne foreign key (numVol) references Vols (numVol) on delete cascade,
	constraint fk_effectueres foreign key (numClient) references Clients (numClient) on delete cascade,
	constraint fk_res_place2 foreign key (numPlace) references Places (numPlace) on delete cascade
);

CREATE OR REPLACE PROCEDURE ANNULATION_RESERVATION (
	   p_num_r IN RESERVATIONS.NUMRESERVATION%TYPE,
	   p_num_c IN RESERVATIONS.NUMCLIENT%TYPE,
	   o_update OUT RESERVATIONS.NUMCLIENT%TYPE)
IS
BEGIN
	--Recherche de l'existante de la réservation
	BEGIN
      SELECT NVL(NUMRESERVATION, 0) INTO o_update
      FROM RESERVATIONS
      WHERE NUMRESERVATION = p_num_r AND NUMCLIENT = p_num_c;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        o_update := 0;
    END;	   
	
   -- annulation de la réservation
   IF o_update != 0 THEN   
	DELETE FROM RESERVATIONS
	WHERE NUMRESERVATION = p_num_r;
   END IF;

  COMMIT;

END;
/

CREATE OR REPLACE PROCEDURE CHECK_EFFECTUE (
p_numV IN EFFECTUE.NUMVOL%TYPE,
p_numA IN EFFECTUE.NUMAVION%TYPE,
p_numH IN EFFECTUE.NUMHOTESSE%TYPE,
p_numP IN EFFECTUE.NUMPILOTE%TYPE
)
IS
CURSOR EffectueCur IS SELECT * FROM Effectue;
BEGIN
		FOR EffectueRec in EffectueCur loop
			IF EffectueRec.NUMVOL= p_numV THEN
				--msg:='Vol Déjà Enregister !';
				RAISE_APPLICATION_ERROR(-20205,
				'Vol déjà affecter !')	;
				ELSIF EffectueRec.NUMAVION = p_numA THEN
					--msg:='Avion Déjà Enregister !';
								RAISE_APPLICATION_ERROR(-20205,
								'Avion déjà affecter !');
				ELSIF EffectueRec.NUMHOTESSE = p_numH THEN
					--msg:='Hotesse Déjà Enregister !';
								RAISE_APPLICATION_ERROR(-20205,
								' Hotesse déjà affecter !');
				ELSIF EffectueRec.NUMPILOTE = p_numP THEN
								RAISE_APPLICATION_ERROR(-20205,
								'Pilote déjà affecter !');
				ELSE
					INSERT INTO Effectue (NUMVOL,NUMAVION,NUMHOTESSE,NUMPILOTE)
					VALUES(p_numV,p_numA,p_numH,p_numP);
					--msg:='Affectation Effectuer !';
			END IF;
		END LOOP;
END;
/

CREATE OR REPLACE PROCEDURE CHECK_AFFECTATION (
p_numA IN AFFECTE.NUMAVION%TYPE,
p_numH IN AFFECTE.NUMHOTESSE%TYPE,
p_numP IN AFFECTE.NUMPILOTE%TYPE
)
IS
CURSOR AffectCur IS SELECT * FROM Affecte;
BEGIN
		FOR AffectA in AffectCur loop
			IF AffectA.NUMAVION = p_numA THEN
				RAISE_APPLICATION_ERROR(-20205,
				'Avion déjà affecter !')	;
				ELSIF AffectA.NUMHOTESSE = p_numH THEN
								RAISE_APPLICATION_ERROR(-20205,
								'Hotesse déjà affecter !');
				ELSIF AffectA.NUMPILOTE = p_numP THEN
								RAISE_APPLICATION_ERROR(-20205,
								'Pilote déjà affecter !');
				ELSE
					INSERT INTO Affecte (NUMAVION,NUMHOTESSE,NUMPILOTE)
					VALUES(p_numA,p_numH,p_numP);
			END IF;
		END LOOP;
END;
/

CREATE OR REPLACE PROCEDURE AFFICHE_VOL (
p_aerodep IN VOLS.AERO_DEP%TYPE,
p_aeroarrv IN VOLS.AERO_ARRV%TYPE,
v_numVol OUT VOLS.NUMVOL%TYPE,
v_horaire OUT VOLS.horaire%TYPE,
v_date_dep OUT VOLS.DATE_DEP%TYPE
)
IS
BEGIN
	SELECT numVol,horaire,date_dep INTO 
	v_numVol,v_horaire,v_date_dep
	FROM VOLS
	WHERE p_aerodep=aero_dep AND p_aeroarrv=aero_arrv;
EXCEPTION
	WHEN NO_DATA_FOUND THEN
		RAISE_APPLICATION_ERROR(-20205,'Aucun Résultat');
END;
/
CREATE TRIGGER SUPPRESION_MODELE_AVION
AFTER DELETE
ON ModelesAvion
FOR EACH ROW
BEGIN
DELETE FROM Avions
WHERE modele=:old.modele;
END;
/

CREATE TRIGGER AJOUT_PRIX_PLACE
AFTER DELETE
ON ModelesAvion
FOR EACH ROW
BEGIN
DELETE FROM Avions
WHERE modele=:old.modele;
END;
/