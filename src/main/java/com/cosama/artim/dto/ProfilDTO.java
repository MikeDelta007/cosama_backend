package com.cosama.artim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProfilDTO {
    private long prfl_id;
    private String prfl_libelle;

    private boolean actif;
    private boolean add_billet;
    private boolean add_check_billet;
    private boolean add_clt_compte;
    private boolean add_embarqment;
    private boolean add_facture;
    private boolean add_fret;
    private boolean del_fret_details;
    private boolean add_nav_data;
    private boolean add_passager;
    private boolean add_reglement;
    private boolean add_voyage;
    private boolean bloq_places;
    private boolean cancel_billet;
    private boolean cancel_fret;
    private boolean cancel_voyage;
    private boolean check_fret;
    private boolean del_clt_compte;
    private boolean del_facture;
    private boolean del_passager;
    private boolean del_reglement;
    private boolean do_remboursement;
    private boolean do_rep_surclassment;
    private boolean edit_billet;
    private boolean edit_clt_compte;
    private boolean edit_facture;
    private boolean edit_fret;
    private boolean edit_nav_data;
    private boolean edit_param;
    private boolean edit_passager;
    private boolean edit_reglement;
    private boolean edit_voyage;
    private boolean edition;
    private boolean paye_fret;
    private boolean plan_voyage;
    private boolean pointer_voyage;
    private boolean rechercher;
    private boolean valide;
    private boolean view_etat;
    private boolean view_stat;
    private boolean view_voyage;
    private boolean reclamation;
    private boolean campagne;

    // Liste des utilisateurs associés
    @Singular
    private List<UserDTO> utilisateurs;
}
