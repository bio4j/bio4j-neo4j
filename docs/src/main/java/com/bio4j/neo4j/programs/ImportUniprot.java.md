
 * Copyright (C) 2010-2013  "Bio4j"
 *
 * This file is part of Bio4j
 *
 * Bio4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>


```java
package com.bio4j.neo4jdb.programs;

import com.bio4j.neo4jdb.model.nodes.*;
import com.bio4j.neo4jdb.model.nodes.citation.*;
import com.bio4j.neo4jdb.model.nodes.reactome.ReactomeTermNode;
import com.bio4j.neo4jdb.model.nodes.refseq.GenomeElementNode;
import com.bio4j.neo4jdb.model.relationships.InstituteCountryRel;
import com.bio4j.neo4jdb.model.relationships.IsoformEventGeneratorRel;
import com.bio4j.neo4jdb.model.relationships.SubcellularLocationParentRel;
import com.bio4j.neo4jdb.model.relationships.TaxonParentRel;
import com.bio4j.neo4jdb.model.relationships.aproducts.AlternativeProductInitiationRel;
import com.bio4j.neo4jdb.model.relationships.aproducts.AlternativeProductPromoterRel;
import com.bio4j.neo4jdb.model.relationships.aproducts.AlternativeProductRibosomalFrameshiftingRel;
import com.bio4j.neo4jdb.model.relationships.aproducts.AlternativeProductSplicingRel;
import com.bio4j.neo4jdb.model.relationships.citation.article.ArticleAuthorRel;
import com.bio4j.neo4jdb.model.relationships.citation.article.ArticleJournalRel;
import com.bio4j.neo4jdb.model.relationships.citation.article.ArticleProteinCitationRel;
import com.bio4j.neo4jdb.model.relationships.citation.book.*;
import com.bio4j.neo4jdb.model.relationships.citation.onarticle.OnlineArticleAuthorRel;
import com.bio4j.neo4jdb.model.relationships.citation.onarticle.OnlineArticleJournalRel;
import com.bio4j.neo4jdb.model.relationships.citation.onarticle.OnlineArticleProteinCitationRel;
import com.bio4j.neo4jdb.model.relationships.citation.patent.PatentAuthorRel;
import com.bio4j.neo4jdb.model.relationships.citation.patent.PatentProteinCitationRel;
import com.bio4j.neo4jdb.model.relationships.citation.submission.SubmissionAuthorRel;
import com.bio4j.neo4jdb.model.relationships.citation.submission.SubmissionDbRel;
import com.bio4j.neo4jdb.model.relationships.citation.submission.SubmissionProteinCitationRel;
import com.bio4j.neo4jdb.model.relationships.citation.thesis.ThesisAuthorRel;
import com.bio4j.neo4jdb.model.relationships.citation.thesis.ThesisInstituteRel;
import com.bio4j.neo4jdb.model.relationships.citation.thesis.ThesisProteinCitationRel;
import com.bio4j.neo4jdb.model.relationships.citation.uo.UnpublishedObservationAuthorRel;
import com.bio4j.neo4jdb.model.relationships.citation.uo.UnpublishedObservationProteinCitationRel;
import com.bio4j.neo4jdb.model.relationships.comment.*;
import com.bio4j.neo4jdb.model.relationships.features.*;
import com.bio4j.neo4jdb.model.relationships.protein.*;
import com.bio4j.neo4jdb.model.util.Bio4jManager;
import com.bio4j.neo4jdb.model.util.UniprotStuff;
import com.ohnosequences.util.Executable;
import com.ohnosequences.xml.model.bio4j.UniprotDataXML;
import com.ohnosequences.xml.api.model.XMLElement;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.jdom2.Element;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.index.lucene.unsafe.batchinsert.LuceneBatchInserterIndexProvider;
import org.neo4j.unsafe.batchinsert.*;

/**
 * This class deals with the main part of Bio4j importing process.
 * ImportGeneOntology importation must have been performed prior to this step.
 * Features, comments, GeneOntology annotations and all information directly
 * related to entries are imported in this step, (except protein interactions
 * and isoform sequences).
 *
 * @author Pablo Pareja Tobes <ppareja@era7.com>
 */
public class ImportUniprot implements Executable {

    private static final Logger logger = Logger.getLogger("ImportUniprot");
    private static FileHandler fh;
    //------------------nodes properties maps-----------------------------------
    public static Map<String, Object> organismProperties = new HashMap<>();
    public static Map<String, Object> proteinProperties = new HashMap<>();
    public static Map<String, Object> keywordProperties = new HashMap<>();
    public static Map<String, Object> subcellularLocationProperties = new HashMap<>();
    public static Map<String, Object> interproProperties = new HashMap<>();
    public static Map<String, Object> pfamProperties = new HashMap<>();
    public static Map<String, Object> taxonProperties = new HashMap<>();
    public static Map<String, Object> datasetProperties = new HashMap<>();
    public static Map<String, Object> personProperties = new HashMap<>();
    public static Map<String, Object> consortiumProperties = new HashMap<>();
    public static Map<String, Object> instituteProperties = new HashMap<>();
    public static Map<String, Object> thesisProperties = new HashMap<>();
    public static Map<String, Object> bookProperties = new HashMap<>();
    public static Map<String, Object> patentProperties = new HashMap<>();
    public static Map<String, Object> articleProperties = new HashMap<>();
    public static Map<String, Object> submissionProperties = new HashMap<>();
    public static Map<String, Object> onlineArticleProperties = new HashMap<>();
    public static Map<String, Object> unpublishedObservationProperties = new HashMap<>();
    public static Map<String, Object> publisherProperties = new HashMap<>();
    public static Map<String, Object> cityProperties = new HashMap<>();
    public static Map<String, Object> journalProperties = new HashMap<>();
    public static Map<String, Object> onlineJournalProperties = new HashMap<>();
    public static Map<String, Object> countryProperties = new HashMap<>();
    public static Map<String, Object> isoformProperties = new HashMap<>();
    public static Map<String, Object> commentTypeProperties = new HashMap<>();
    public static Map<String, Object> featureTypeProperties = new HashMap<>();
    public static Map<String, Object> reactomeTermProperties = new HashMap<>();
    public static Map<String, Object> dbProperties = new HashMap<>();
    //---------------------------------------------------------------------
    //-------------------relationships properties maps--------------------------
    public static Map<String, Object> proteinGoProperties = new HashMap<>();
    public static Map<String, Object> proteinSubcellularLocationProperties = new HashMap<>();
    public static Map<String, Object> bookProteinCitationProperties = new HashMap<>();
    public static Map<String, Object> articleJournalProperties = new HashMap<>();
    public static Map<String, Object> onlineArticleJournalProperties = new HashMap<>();
    public static Map<String, Object> commentProperties = new HashMap<>();
    public static Map<String, Object> onlineInformationCommentProperties = new HashMap<>();
    public static Map<String, Object> biophysicochemicalCommentProperties = new HashMap<>();
    public static Map<String, Object> rnaEditingCommentProperties = new HashMap<>();
    public static Map<String, Object> massSpectrometryCommentProperties = new HashMap<>();
    public static Map<String, Object> featureProperties = new HashMap<>();
    public static Map<String, Object> sequenceCautionProperties = new HashMap<>();
    //----------------------------------------------------------------------------
    //--------------------------------relationships------------------------------------------
    public static ProteinGoRel proteinGoRel = new ProteinGoRel(null);
    public static ProteinOrganismRel proteinOrganismRel = new ProteinOrganismRel(null);
    public static TaxonParentRel taxonParentRel = new TaxonParentRel(null);
    public static ProteinKeywordRel proteinKeywordRel = new ProteinKeywordRel(null);
    public static ProteinDatasetRel proteinDatasetRel = new ProteinDatasetRel(null);
    public static ProteinInterproRel proteinInterproRel = new ProteinInterproRel(null);
    public static ProteinPfamRel proteinPfamRel = new ProteinPfamRel(null);
    public static ProteinSubcellularLocationRel proteinSubcellularLocationRel = new ProteinSubcellularLocationRel(null);
    public static SubcellularLocationParentRel subcellularLocationParentRel = new SubcellularLocationParentRel(null);
    public static ThesisAuthorRel thesisAuthorRel = new ThesisAuthorRel(null);
    public static ThesisInstituteRel thesisInstituteRel = new ThesisInstituteRel(null);
    public static ThesisProteinCitationRel thesisProteinCitationRel = new ThesisProteinCitationRel(null);
    public static PatentAuthorRel patentAuthorRel = new PatentAuthorRel(null);
    public static PatentProteinCitationRel patentProteinCitationRel = new PatentProteinCitationRel(null);
    public static SubmissionAuthorRel submissionAuthorRel = new SubmissionAuthorRel(null);
    public static SubmissionProteinCitationRel submissionProteinCitationRel = new SubmissionProteinCitationRel(null);
    public static SubmissionDbRel submissionDbRel = new SubmissionDbRel(null);
    public static BookAuthorRel bookAuthorRel = new BookAuthorRel(null);
    public static BookProteinCitationRel bookProteinCitationRel = new BookProteinCitationRel(null);
    public static BookEditorRel bookEditorRel = new BookEditorRel(null);
    public static BookCityRel bookCityRel = new BookCityRel(null);
    public static BookPublisherRel bookPublisherRel = new BookPublisherRel(null);
    public static ArticleAuthorRel articleAuthorRel = new ArticleAuthorRel(null);
    public static ArticleJournalRel articleJournalRel = new ArticleJournalRel(null);
    public static ArticleProteinCitationRel articleProteinCitationRel = new ArticleProteinCitationRel(null);
    public static OnlineArticleAuthorRel onlineArticleAuthorRel = new OnlineArticleAuthorRel(null);
    public static OnlineArticleJournalRel onlineArticleJournalRel = new OnlineArticleJournalRel(null);
    public static OnlineArticleProteinCitationRel onlineArticleProteinCitationRel = new OnlineArticleProteinCitationRel(null);
    public static UnpublishedObservationAuthorRel unpublishedObservationAuthorRel = new UnpublishedObservationAuthorRel(null);
    public static UnpublishedObservationProteinCitationRel unpublishedObservationProteinCitationRel = new UnpublishedObservationProteinCitationRel(null);
    public static InstituteCountryRel instituteCountryRel = new InstituteCountryRel(null);
    public static IsoformEventGeneratorRel isoformEventGeneratorRel = new IsoformEventGeneratorRel(null);
    public static ProteinIsoformRel proteinIsoformRel = new ProteinIsoformRel(null);
    public static ProteinErroneousGeneModelPredictionRel proteinErroneousGeneModelPredictionRel = new ProteinErroneousGeneModelPredictionRel(null);
    public static ProteinErroneousInitiationRel proteinErroneousInitiationRel = new ProteinErroneousInitiationRel(null);
    public static ProteinErroneousTerminationRel proteinErroneousTerminationRel = new ProteinErroneousTerminationRel(null);
    public static ProteinErroneousTranslationRel proteinErroneousTranslationRel = new ProteinErroneousTranslationRel(null);
    public static ProteinFrameshiftRel proteinFrameshiftRel = new ProteinFrameshiftRel(null);
    public static ProteinMiscellaneousDiscrepancyRel proteinMiscellaneousDiscrepancyRel = new ProteinMiscellaneousDiscrepancyRel(null);
    public static ProteinGenomeElementRel proteinGenomeElementRel = new ProteinGenomeElementRel(null);
    public static ProteinReactomeRel proteinReactomeRel = new ProteinReactomeRel(null);
    public static ProteinEnzymaticActivityRel proteinEnzymaticActivityRel = new ProteinEnzymaticActivityRel(null);
    //----comment relationships-----
    public static AllergenCommentRel allergenCommentRel = new AllergenCommentRel(null);
    public static BioPhysicoChemicalPropertiesCommentRel bioPhysicoChemicalPropertiesCommentRel = new BioPhysicoChemicalPropertiesCommentRel(null);
    public static BiotechnologyCommentRel biotechnologyCommentRel = new BiotechnologyCommentRel(null);
    public static CatalyticActivityCommentRel catalyticActivityCommentRel = new CatalyticActivityCommentRel(null);
    public static CautionCommentRel cautionCommentRel = new CautionCommentRel(null);
    public static CofactorCommentRel cofactorCommentRel = new CofactorCommentRel(null);
    public static DevelopmentalStageCommentRel developmentalStageCommentRel = new DevelopmentalStageCommentRel(null);
    public static DiseaseCommentRel diseaseCommentRel = new DiseaseCommentRel(null);
    public static DisruptionPhenotypeCommentRel disruptionPhenotypeCommentRel = new DisruptionPhenotypeCommentRel(null);
    public static DomainCommentRel domainCommentRel = new DomainCommentRel(null);
    public static EnzymeRegulationCommentRel enzymeRegulationCommentRel = new EnzymeRegulationCommentRel(null);
    public static FunctionCommentRel functionCommentRel = new FunctionCommentRel(null);
    public static InductionCommentRel inductionCommentRel = new InductionCommentRel(null);
    public static MassSpectrometryCommentRel massSpectrometryCommentRel = new MassSpectrometryCommentRel(null);
    public static MiscellaneousCommentRel miscellaneousCommentRel = new MiscellaneousCommentRel(null);
    public static OnlineInformationCommentRel onlineInformationCommentRel = new OnlineInformationCommentRel(null);
    public static PathwayCommentRel pathwayCommentRel = new PathwayCommentRel(null);
    public static PharmaceuticalCommentRel pharmaceuticalCommentRel = new PharmaceuticalCommentRel(null);
    public static PolymorphismCommentRel polymorphismCommentRel = new PolymorphismCommentRel(null);
    public static PostTranslationalModificationCommentRel postTranslationalModificationCommentRel = new PostTranslationalModificationCommentRel(null);
    public static RnaEditingCommentRel rnaEditingCommentRel = new RnaEditingCommentRel(null);
    public static SimilarityCommentRel similarityCommentRel = new SimilarityCommentRel(null);
    public static SubunitCommentRel subunitCommentRel = new SubunitCommentRel(null);
    public static TissueSpecificityCommentRel tissueSpecificityCommentRel = new TissueSpecificityCommentRel(null);
    public static ToxicDoseCommentRel toxicDoseCommentRel = new ToxicDoseCommentRel(null);
    //features relationships------------------------------------------
    public static ActiveSiteFeatureRel activeSiteFeatureRel = new ActiveSiteFeatureRel(null);
    public static BindingSiteFeatureRel bindingSiteFeatureRel = new BindingSiteFeatureRel(null);
    public static CalciumBindingRegionFeatureRel calciumBindingRegionFeatureRel = new CalciumBindingRegionFeatureRel(null);
    public static ChainFeatureRel chainFeatureRel = new ChainFeatureRel(null);
    public static CoiledCoilRegionFeatureRel coiledCoilRegionFeatureRel = new CoiledCoilRegionFeatureRel(null);
    public static CompositionallyBiasedRegionFeatureRel compositionallyBiasedRegionFeatureRel = new CompositionallyBiasedRegionFeatureRel(null);
    public static CrossLinkFeatureRel crossLinkFeatureRel = new CrossLinkFeatureRel(null);
    public static DisulfideBondFeatureRel disulfideBondFeatureRel = new DisulfideBondFeatureRel(null);
    public static DnaBindingRegionFeatureRel dnaBindingRegionFeatureRel = new DnaBindingRegionFeatureRel(null);
    public static DomainFeatureRel domainFeatureRel = new DomainFeatureRel(null);
    public static GlycosylationSiteFeatureRel glycosylationSiteFeatureRel = new GlycosylationSiteFeatureRel(null);
    public static HelixFeatureRel helixFeatureRel = new HelixFeatureRel(null);
    public static InitiatorMethionineFeatureRel initiatorMethionineFeatureRel = new InitiatorMethionineFeatureRel(null);
    public static IntramembraneRegionFeatureRel intramembraneRegionFeatureRel = new IntramembraneRegionFeatureRel(null);
    public static LipidMoietyBindingRegionFeatureRel lipidMoietyBindingRegionFeatureRel = new LipidMoietyBindingRegionFeatureRel(null);
    public static MetalIonBindingSiteFeatureRel metalIonBindingSiteFeatureRel = new MetalIonBindingSiteFeatureRel(null);
    public static ModifiedResidueFeatureRel modifiedResidueFeatureRel = new ModifiedResidueFeatureRel(null);
    public static MutagenesisSiteFeatureRel mutagenesisSiteFeatureRel = new MutagenesisSiteFeatureRel(null);
    public static NonConsecutiveResiduesFeatureRel nonConsecutiveResiduesFeatureRel = new NonConsecutiveResiduesFeatureRel(null);
    public static NonStandardAminoAcidFeatureRel nonStandardAminoAcidFeatureRel = new NonStandardAminoAcidFeatureRel(null);
    public static NonTerminalResidueFeatureRel nonTerminalResidueFeatureRel = new NonTerminalResidueFeatureRel(null);
    public static NucleotidePhosphateBindingRegionFeatureRel nucleotidePhosphateBindingRegionFeatureRel = new NucleotidePhosphateBindingRegionFeatureRel(null);
    public static PeptideFeatureRel peptideFeatureRel = new PeptideFeatureRel(null);
    public static PropeptideFeatureRel propeptideFeatureRel = new PropeptideFeatureRel(null);
    public static RegionOfInterestFeatureRel regionOfInterestFeatureRel = new RegionOfInterestFeatureRel(null);
    public static RepeatFeatureRel repeatFeatureRel = new RepeatFeatureRel(null);
    public static SequenceConflictFeatureRel sequenceConflictFeatureRel = new SequenceConflictFeatureRel(null);
    public static SequenceVariantFeatureRel sequenceVariantFeatureRel = new SequenceVariantFeatureRel(null);
    public static ShortSequenceMotifFeatureRel shortSequenceMotifFeatureRel = new ShortSequenceMotifFeatureRel(null);
    public static SignalPeptideFeatureRel signalPeptideFeatureRel = new SignalPeptideFeatureRel(null);
    public static SiteFeatureRel siteFeatureRel = new SiteFeatureRel(null);
    public static SpliceVariantFeatureRel spliceVariantFeatureRel = new SpliceVariantFeatureRel(null);
    public static StrandFeatureRel strandFeatureRel = new StrandFeatureRel(null);
    public static TopologicalDomainFeatureRel topologicalDomainFeatureRel = new TopologicalDomainFeatureRel(null);
    public static TransitPeptideFeatureRel transitPeptideFeatureRel = new TransitPeptideFeatureRel(null);
    public static TransmembraneRegionFeatureRel transmembraneRegionFeatureRel = new TransmembraneRegionFeatureRel(null);
    public static TurnFeatureRel turnFeatureRel = new TurnFeatureRel(null);
    public static UnsureResidueFeatureRel unsureResidueFeatureRel = new UnsureResidueFeatureRel(null);
    public static ZincFingerRegionFeatureRel zincFingerRegionFeatureRel = new ZincFingerRegionFeatureRel(null);
    //------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------
    //-----other things....------
    public static long alternativeProductInitiationId;
    public static long alternativeProductPromoterId;
    public static long alternativeProductSplicingId;
    public static long alternativeProductRibosomalFrameshiftingId;
    public static long seqCautionErroneousInitiationId;
    public static long seqCautionErroneousTranslationId;
    public static long seqCautionFrameshiftId;
    public static long seqCautionErroneousTerminationId;
    public static long seqCautionMiscellaneousDiscrepancyId;
    public static long seqCautionErroneousGeneModelPredictionId;
    //---------------------------------
    //--------indexing API constans-----
    private static String PROVIDER_ST = "provider";
    private static String EXACT_ST = "exact";
    private static String FULL_TEXT_ST = "fulltext";
    private static String LUCENE_ST = "lucene";
    private static String TYPE_ST = "type";
    //-----------------------------------

    @Override
    public void execute(ArrayList<String> array) {
        String[] args = new String[array.size()];
        for (int i = 0; i < array.size(); i++) {
            args[i] = array.get(i);
        }
        main(args);
    }

    public static void main(String[] args) {

        if (args.length != 4) {
            System.out.println("This program expects the following parameters: \n"
                    + "1. Uniprot xml filename \n"
                    + "2. Bio4j DB folder \n"
                    + "3. batch inserter .properties file \n"
                    + "4. Config XML file");
        } else {

            long initTime = System.nanoTime();

            File inFile = new File(args[0]);
            File configFile = new File(args[3]);

            String currentAccessionId = "";

            BatchInserter inserter = null;
            BatchInserterIndexProvider indexProvider = null;

            BufferedWriter enzymeIdsNotFoundBuff = null;
            BufferedWriter statsBuff = null;

            int proteinCounter = 0;
            int limitForPrintingOut = 10000;

            try {

                // This block configures the logger with handler and formatter
                fh = new FileHandler("ImportUniprot" + args[0].split("\\.")[0] + ".log", false);

                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);
                logger.addHandler(fh);
                logger.setLevel(Level.ALL);

                System.out.println("Reading conf file...");
                BufferedReader reader = new BufferedReader(new FileReader(configFile));
                String line;
                StringBuilder stBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    stBuilder.append(line);
                }
                reader.close();

                UniprotDataXML uniprotDataXML = new UniprotDataXML(stBuilder.toString());

                //---creating writer for enzymes not found file-----
                enzymeIdsNotFoundBuff = new BufferedWriter(new FileWriter(new File("EnzymeIdsNotFound.log")));

                //---creating writer for stats file-----
                statsBuff = new BufferedWriter(new FileWriter(new File("ImportUniprotStats_" + inFile.getName().split("\\.")[0] + ".txt")));

                // create the batch inserter
                inserter = BatchInserters.inserter(args[1], MapUtil.load(new File(args[2])));

                // create the batch index service
                indexProvider = new LuceneBatchInserterIndexProvider(inserter);

                //-----------------create batch indexes----------------------------------
                //----------------------------------------------------------------------
                BatchInserterIndex proteinAccessionIndex = indexProvider.nodeIndex(ProteinNode.PROTEIN_ACCESSION_INDEX,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
                BatchInserterIndex proteinFullNameFullTextIndex = indexProvider.nodeIndex(ProteinNode.PROTEIN_FULL_NAME_FULL_TEXT_INDEX,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, FULL_TEXT_ST));
                BatchInserterIndex proteinGeneNamesFullTextIndex = indexProvider.nodeIndex(ProteinNode.PROTEIN_GENE_NAMES_FULL_TEXT_INDEX,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, FULL_TEXT_ST));
                BatchInserterIndex proteinEnsemblPlantsIndex = indexProvider.nodeIndex(ProteinNode.PROTEIN_ENSEMBL_PLANTS_INDEX,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
                BatchInserterIndex datasetNameIndex = indexProvider.nodeIndex(DatasetNode.DATASET_NAME_INDEX,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
                BatchInserterIndex keywordIdIndex = indexProvider.nodeIndex(KeywordNode.KEYWORD_ID_INDEX,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
                BatchInserterIndex keywordNameIndex = indexProvider.nodeIndex(KeywordNode.KEYWORD_NAME_INDEX,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
                BatchInserterIndex interproIdIndex = indexProvider.nodeIndex(InterproNode.INTERPRO_ID_INDEX,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
                BatchInserterIndex pfamIdIndex = indexProvider.nodeIndex(PfamNode.PFAM_ID_INDEX,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
                BatchInserterIndex goTermIdIndex = indexProvider.nodeIndex(GoTermNode.GO_TERM_ID_INDEX,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
                BatchInserterIndex organismScientificNameIndex = indexProvider.nodeIndex(OrganismNode.ORGANISM_SCIENTIFIC_NAME_INDEX,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
                BatchInserterIndex organismNcbiTaxonomyIdIndex = indexProvider.nodeIndex(OrganismNode.ORGANISM_NCBI_TAXONOMY_ID_INDEX,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
                BatchInserterIndex taxonNameIndex = indexProvider.nodeIndex(TaxonNode.TAXON_NAME_INDEX,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
                BatchInserterIndex genomeElementVersionIndex = indexProvider.nodeIndex(GenomeElementNode.GENOME_ELEMENT_VERSION_INDEX,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
                BatchInserterIndex reactomeTermIdIndex = indexProvider.nodeIndex(ReactomeTermNode.REACTOME_TERM_ID_INDEX,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
                BatchInserterIndex enzymeIdIndex = indexProvider.nodeIndex(EnzymeNode.ENZYME_ID_INDEX,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
                BatchInserterIndex nodeTypeIndex = indexProvider.nodeIndex(Bio4jManager.NODE_TYPE_INDEX_NAME,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
                BatchInserterIndex mainNodesIndex = indexProvider.nodeIndex(Bio4jManager.MAIN_NODES_INDEX_NAME,
                        MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
                //----------------------------------------------------------------------
                //----------------------------------------------------------------------

                reader = new BufferedReader(new FileReader(inFile));
                StringBuilder entryStBuilder = new StringBuilder();

                
                //----------------------------------------------------------------------
                //------------------------looking up for main nodes---------------------
                alternativeProductInitiationId = mainNodesIndex.get(Bio4jManager.MAIN_NODES_INDEX_NAME, Bio4jManager.ALTERNATIVE_PRODUCT_INITIATION).getSingle();
                alternativeProductPromoterId = mainNodesIndex.get(Bio4jManager.MAIN_NODES_INDEX_NAME, Bio4jManager.ALTERNATIVE_PRODUCT_PROMOTER).getSingle();
                alternativeProductSplicingId = mainNodesIndex.get(Bio4jManager.MAIN_NODES_INDEX_NAME, Bio4jManager.ALTERNATIVE_PRODUCT_SPLICING).getSingle();
                alternativeProductRibosomalFrameshiftingId = mainNodesIndex.get(Bio4jManager.MAIN_NODES_INDEX_NAME, Bio4jManager.ALTERNATIVE_PRODUCT_RIBOSOMAL_FRAMESHIFTING).getSingle();
                seqCautionErroneousInitiationId = mainNodesIndex.get(Bio4jManager.MAIN_NODES_INDEX_NAME, Bio4jManager.SEQUENCE_CAUTION_ERRONEOUS_INITIATION).getSingle();
                seqCautionErroneousTranslationId = mainNodesIndex.get(Bio4jManager.MAIN_NODES_INDEX_NAME, Bio4jManager.SEQUENCE_CAUTION_ERRONEOUS_TRANSLATION).getSingle();
                seqCautionFrameshiftId = mainNodesIndex.get(Bio4jManager.MAIN_NODES_INDEX_NAME, Bio4jManager.SEQUENCE_CAUTION_FRAMESHIFT).getSingle();
                seqCautionErroneousTerminationId = mainNodesIndex.get(Bio4jManager.MAIN_NODES_INDEX_NAME, Bio4jManager.SEQUENCE_CAUTION_ERRONEOUS_TERMINATION).getSingle();
                seqCautionMiscellaneousDiscrepancyId = mainNodesIndex.get(Bio4jManager.MAIN_NODES_INDEX_NAME, Bio4jManager.SEQUENCE_CAUTION_MISCELLANEOUS_DISCREPANCY).getSingle();
                seqCautionErroneousGeneModelPredictionId = mainNodesIndex.get(Bio4jManager.MAIN_NODES_INDEX_NAME, Bio4jManager.SEQUENCE_CAUTION_ERRONEOUS_GENE_MODEL_PREDICTION).getSingle();
                //----------------------------------------------------------------------

                //----------------------------------------------------------------------------------
                //---------------------initializing node type properties----------------------------
                organismProperties.put(OrganismNode.NODE_TYPE_PROPERTY, OrganismNode.NODE_TYPE);
                proteinProperties.put(ProteinNode.NODE_TYPE_PROPERTY, ProteinNode.NODE_TYPE);
                keywordProperties.put(KeywordNode.NODE_TYPE_PROPERTY, KeywordNode.NODE_TYPE);
                subcellularLocationProperties.put(SubcellularLocationNode.NODE_TYPE_PROPERTY, SubcellularLocationNode.NODE_TYPE);
                interproProperties.put(InterproNode.NODE_TYPE_PROPERTY, InterproNode.NODE_TYPE);
                pfamProperties.put(PfamNode.NODE_TYPE_PROPERTY, PfamNode.NODE_TYPE);
                taxonProperties.put(TaxonNode.NODE_TYPE_PROPERTY, TaxonNode.NODE_TYPE);
                datasetProperties.put(DatasetNode.NODE_TYPE_PROPERTY, DatasetNode.NODE_TYPE);
                personProperties.put(PersonNode.NODE_TYPE_PROPERTY, PersonNode.NODE_TYPE);
                consortiumProperties.put(ConsortiumNode.NODE_TYPE_PROPERTY, ConsortiumNode.NODE_TYPE);
                instituteProperties.put(InstituteNode.NODE_TYPE_PROPERTY, InstituteNode.NODE_TYPE);
                thesisProperties.put(ThesisNode.NODE_TYPE_PROPERTY, ThesisNode.NODE_TYPE);
                bookProperties.put(BookNode.NODE_TYPE_PROPERTY, BookNode.NODE_TYPE);
                patentProperties.put(PatentNode.NODE_TYPE_PROPERTY, PatentNode.NODE_TYPE);
                articleProperties.put(ArticleNode.NODE_TYPE_PROPERTY, ArticleNode.NODE_TYPE);
                submissionProperties.put(SubmissionNode.NODE_TYPE_PROPERTY, SubmissionNode.NODE_TYPE);
                onlineArticleProperties.put(OnlineArticleNode.NODE_TYPE_PROPERTY, OnlineArticleNode.NODE_TYPE);
                unpublishedObservationProperties.put(UnpublishedObservationNode.NODE_TYPE_PROPERTY, UnpublishedObservationNode.NODE_TYPE);
                publisherProperties.put(PublisherNode.NODE_TYPE_PROPERTY, PublisherNode.NODE_TYPE);
                cityProperties.put(CityNode.NODE_TYPE_PROPERTY, CityNode.NODE_TYPE);
                journalProperties.put(JournalNode.NODE_TYPE_PROPERTY, JournalNode.NODE_TYPE);
                onlineJournalProperties.put(OnlineJournalNode.NODE_TYPE_PROPERTY, OnlineJournalNode.NODE_TYPE);
                countryProperties.put(CountryNode.NODE_TYPE_PROPERTY, CountryNode.NODE_TYPE);
                isoformProperties.put(IsoformNode.NODE_TYPE_PROPERTY, IsoformNode.NODE_TYPE);
                commentTypeProperties.put(CommentTypeNode.NODE_TYPE_PROPERTY, CommentTypeNode.NODE_TYPE);
                featureTypeProperties.put(FeatureTypeNode.NODE_TYPE_PROPERTY, FeatureTypeNode.NODE_TYPE);
                //-----------------------------------------------------------------------------------------
                //-----------------------------------------------------------------------------------------

                while ((line = reader.readLine()) != null) {
                    if (line.trim().startsWith("<" + UniprotStuff.ENTRY_TAG_NAME)) {

                        while (!line.trim().startsWith("</" + UniprotStuff.ENTRY_TAG_NAME + ">")) {
                            entryStBuilder.append(line);
                            line = reader.readLine();
                        }
                        //linea final del organism
                        entryStBuilder.append(line);
                        //System.out.println("organismStBuilder.toString() = " + organismStBuilder.toString());
                        XMLElement entryXMLElem = new XMLElement(entryStBuilder.toString());
                        entryStBuilder.delete(0, entryStBuilder.length());

                        String modifiedDateSt = entryXMLElem.asJDomElement().getAttributeValue(UniprotStuff.ENTRY_MODIFIED_DATE_ATTRIBUTE);

                        String accessionSt = entryXMLElem.asJDomElement().getChildText(UniprotStuff.ENTRY_ACCESSION_TAG_NAME);
                        String nameSt = entryXMLElem.asJDomElement().getChildText(UniprotStuff.ENTRY_NAME_TAG_NAME);
                        String fullNameSt = getProteinFullName(entryXMLElem.asJDomElement().getChild(UniprotStuff.PROTEIN_TAG_NAME));
                        String shortNameSt = getProteinShortName(entryXMLElem.asJDomElement().getChild(UniprotStuff.PROTEIN_TAG_NAME));

                        if (shortNameSt == null) {
                            shortNameSt = "";
                        }
                        if (fullNameSt == null) {
                            fullNameSt = "";
                        }

                        currentAccessionId = accessionSt;

                        //-----------alternative accessions-------------
                        ArrayList<String> alternativeAccessions = new ArrayList<>();
                        List<Element> altAccessionsList = entryXMLElem.asJDomElement().getChildren(UniprotStuff.ENTRY_ACCESSION_TAG_NAME);
                        for (int i = 1; i < altAccessionsList.size(); i++) {
                            alternativeAccessions.add(altAccessionsList.get(i).getText());
                        }
                        proteinProperties.put(ProteinNode.ALTERNATIVE_ACCESSIONS_PROPERTY, convertToStringArray(alternativeAccessions));

                        //-----db references-------------
                        String pirIdSt = "";
                        String keggIdSt = "";
                        String ensemblIdSt = "";
                        String uniGeneIdSt = "";
                        String arrayExpressIdSt = "";

                        List<Element> dbReferenceList = entryXMLElem.asJDomElement().getChildren(UniprotStuff.DB_REFERENCE_TAG_NAME);
                        ArrayList<String> emblCrossReferences = new ArrayList<>();
                        ArrayList<String> refseqReferences = new ArrayList<>();
                        ArrayList<String> enzymeDBReferences = new ArrayList<>();
                        ArrayList<String> ensemblPlantsReferences = new ArrayList<>();
                        HashMap<String, String> reactomeReferences = new HashMap<>();

                        for (Element dbReferenceElem : dbReferenceList) {
                            String refId = dbReferenceElem.getAttributeValue("id");
                            switch (dbReferenceElem.getAttributeValue(UniprotStuff.DB_REFERENCE_TYPE_ATTRIBUTE)) {
                                case "Ensembl":
                                    ensemblIdSt = refId;
                                    break;
                                case "PIR":
                                    pirIdSt = refId;
                                    break;
                                case "UniGene":
                                    uniGeneIdSt = refId;
                                    break;
                                case "KEGG":
                                    keggIdSt = refId;
                                    break;
                                case "EMBL":
                                    emblCrossReferences.add(refId);
                                    break;
                                case "EC":
                                    enzymeDBReferences.add(refId);
                                    break;
                                case "ArrayExpress":
                                    arrayExpressIdSt = refId;
                                    break;
                                case "RefSeq":
                                    //refseqReferences.add(refId);
                                    List<Element> children = dbReferenceElem.getChildren("property");
                                    for (Element propertyElem : children) {
                                        if (propertyElem.getAttributeValue("type").equals("nucleotide sequence ID")) {
                                            refseqReferences.add(propertyElem.getAttributeValue("value"));
                                        }
                                    }
                                    break;
                                case "Reactome":
                                    Element propertyElem = dbReferenceElem.getChild("property");
                                    String pathwayName = "";
                                    if (propertyElem.getAttributeValue("type").equals("pathway name")) {
                                        pathwayName = propertyElem.getAttributeValue("value");
                                    }
                                    reactomeReferences.put(refId, pathwayName);
                                    break;
                                case "EnsemblPlants":
                                    ensemblPlantsReferences.add(refId);
                                    break;
                            }

                        }

                        Element sequenceElem = entryXMLElem.asJDomElement().getChild(UniprotStuff.ENTRY_SEQUENCE_TAG_NAME);
                        String sequenceSt = sequenceElem.getText();
                        int seqLength = Integer.parseInt(sequenceElem.getAttributeValue(UniprotStuff.SEQUENCE_LENGTH_ATTRIBUTE));
                        float seqMass = Float.parseFloat(sequenceElem.getAttributeValue(UniprotStuff.SEQUENCE_MASS_ATTRIBUTE));


                        //System.out.println("lalala " + seqMass);
                        proteinProperties.put(ProteinNode.MODIFIED_DATE_PROPERTY, modifiedDateSt);
                        proteinProperties.put(ProteinNode.ACCESSION_PROPERTY, accessionSt);
                        proteinProperties.put(ProteinNode.NAME_PROPERTY, nameSt);
                        proteinProperties.put(ProteinNode.FULL_NAME_PROPERTY, fullNameSt);
                        proteinProperties.put(ProteinNode.SHORT_NAME_PROPERTY, shortNameSt);
                        proteinProperties.put(ProteinNode.SEQUENCE_PROPERTY, sequenceSt);
                        proteinProperties.put(ProteinNode.LENGTH_PROPERTY, seqLength);
                        proteinProperties.put(ProteinNode.MASS_PROPERTY, seqMass);
                        proteinProperties.put(ProteinNode.ARRAY_EXPRESS_ID_PROPERTY, arrayExpressIdSt);
                        proteinProperties.put(ProteinNode.PIR_ID_PROPERTY, pirIdSt);
                        proteinProperties.put(ProteinNode.KEGG_ID_PROPERTY, keggIdSt);
                        proteinProperties.put(ProteinNode.EMBL_REFERENCES_PROPERTY, convertToStringArray(emblCrossReferences));
                        proteinProperties.put(ProteinNode.ENSEMBL_PLANTS_REFERENCES_PROPERTY, convertToStringArray(ensemblPlantsReferences));
                        proteinProperties.put(ProteinNode.ENSEMBL_ID_PROPERTY, ensemblIdSt);
                        proteinProperties.put(ProteinNode.UNIGENE_ID_PROPERTY, uniGeneIdSt);

                        //---------------gene-names-------------------
                        Element geneElement = entryXMLElem.asJDomElement().getChild(UniprotStuff.GENE_TAG_NAME);
                        ArrayList<String> geneNames = new ArrayList<>();
                        if (geneElement != null) {
                            List<Element> genesList = geneElement.getChildren(UniprotStuff.GENE_NAME_TAG_NAME);
                            for (Element geneNameElem : genesList) {
                                geneNames.add(geneNameElem.getText());
                            }
                        }
                        proteinProperties.put(ProteinNode.GENE_NAMES_PROPERTY, convertToStringArray(geneNames));
                        //-----------------------------------------


                        long currentProteinId = inserter.createNode(proteinProperties);
                        proteinAccessionIndex.add(currentProteinId, MapUtil.map(ProteinNode.PROTEIN_ACCESSION_INDEX, accessionSt));

                        //indexing protein by alternative accessions
                        for (String altAccessionSt : alternativeAccessions) {
                            proteinAccessionIndex.add(currentProteinId, MapUtil.map(ProteinNode.PROTEIN_ACCESSION_INDEX, altAccessionSt));
                        }
                        //---flushing protein accession index----
                        proteinAccessionIndex.flush();

                        //---adding protein node to node_type index----
                        nodeTypeIndex.add(currentProteinId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, ProteinNode.NODE_TYPE));

                        //indexing protein by full name
                        if (!fullNameSt.isEmpty()) {
                            proteinFullNameFullTextIndex.add(currentProteinId, MapUtil.map(ProteinNode.PROTEIN_FULL_NAME_FULL_TEXT_INDEX, fullNameSt));

                            //System.out.println(fullNameSt.toUpperCase() + " , " + currentProteinId);
                        }


                        //indexing protein by gene names
                        String geneNamesStToBeIndexed = "";
                        for (String geneNameSt : geneNames) {
                            geneNamesStToBeIndexed += geneNameSt + " ";
                        }

                        proteinGeneNamesFullTextIndex.add(currentProteinId, MapUtil.map(ProteinNode.PROTEIN_GENE_NAMES_FULL_TEXT_INDEX, geneNamesStToBeIndexed));

                        //indexing protein by Ensembl plants references
                        for (String ensemblPlantRef : ensemblPlantsReferences) {
                            proteinEnsemblPlantsIndex.add(currentProteinId, MapUtil.map(ProteinNode.PROTEIN_ENSEMBL_PLANTS_INDEX, ensemblPlantRef));
                        }


                        //--------------refseq associations----------------
                        if (uniprotDataXML.getRefseq()) {
                            for (String refseqReferenceSt : refseqReferences) {
                                //System.out.println("refseqReferenceSt = " + refseqReferenceSt);
                                IndexHits<Long> hits = genomeElementVersionIndex.get(GenomeElementNode.GENOME_ELEMENT_VERSION_INDEX, refseqReferenceSt);
                                if (hits.hasNext()) {
                                    inserter.createRelationship(currentProteinId, hits.getSingle(), proteinGenomeElementRel, null);
                                } else {
                                    logger.log(Level.INFO, ("GenomeElem not found for: " + currentAccessionId + " , " + refseqReferenceSt));
                                }

                            }
                        }

                        //--------------reactome associations----------------
                        if (uniprotDataXML.getReactome()) {
                            for (String reactomeId : reactomeReferences.keySet()) {
                                long reactomeTermNodeId = -1;
                                IndexHits<Long> reactomeTermIdIndexHits = reactomeTermIdIndex.get(ReactomeTermNode.REACTOME_TERM_ID_INDEX, reactomeId);
                                if (reactomeTermIdIndexHits.hasNext()) {
                                    reactomeTermNodeId = reactomeTermIdIndexHits.getSingle();
                                }
                                if (reactomeTermNodeId < 0) {
                                    reactomeTermProperties.put(ReactomeTermNode.ID_PROPERTY, reactomeId);
                                    reactomeTermProperties.put(ReactomeTermNode.PATHWAY_NAME_PROPERTY, reactomeReferences.get(reactomeId));
                                    reactomeTermNodeId = inserter.createNode(reactomeTermProperties);
                                    reactomeTermIdIndex.add(reactomeTermNodeId, MapUtil.map(ReactomeTermNode.REACTOME_TERM_ID_INDEX, reactomeId));
                                    //----flushing reactome index---
                                    reactomeTermIdIndex.flush();
                                    //---adding reactome term node to node_type index----
                                    nodeTypeIndex.add(reactomeTermNodeId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, ReactomeTermNode.NODE_TYPE));
                                }
                                inserter.createRelationship(currentProteinId, reactomeTermNodeId, proteinReactomeRel, null);
                            }
                        }
                        //-------------------------------------------------------

                        //---------------enzyme db associations----------------------
                        if (uniprotDataXML.getEnzymeDb()) {
                            for (String enzymeDBRef : enzymeDBReferences) {
                                long enzymeNodeId;
                                IndexHits<Long> enzymeIdIndexHits = enzymeIdIndex.get(EnzymeNode.ENZYME_ID_INDEX, enzymeDBRef);
                                if (enzymeIdIndexHits.hasNext()) {
                                    enzymeNodeId = enzymeIdIndexHits.next();
                                    inserter.createRelationship(currentProteinId, enzymeNodeId, proteinEnzymaticActivityRel, null);
                                } else {
                                    enzymeIdsNotFoundBuff.write("Enzyme term: " + enzymeDBRef + " not found.\t" + currentAccessionId);
                                }
                            }
                        }
                        //------------------------------------------------------------


                        //-----comments import---
                        if (uniprotDataXML.getComments()) {
                            importProteinComments(entryXMLElem, inserter, indexProvider, currentProteinId, sequenceSt, uniprotDataXML);
                        }

                        //-----features import----
                        if (uniprotDataXML.getFeatures()) {
                            importProteinFeatures(entryXMLElem, inserter, indexProvider, currentProteinId);
                        }

                        //--------------------------------datasets--------------------------------------------------
                        String proteinDataSetSt = entryXMLElem.asJDomElement().getAttributeValue(UniprotStuff.ENTRY_DATASET_ATTRIBUTE);
                        //long datasetId = indexService.getSingleNode(DatasetNode.DATASET_NAME_INDEX, proteinDataSetSt);
                        long datasetId = -1;
                        IndexHits<Long> datasetNameIndexHits = datasetNameIndex.get(DatasetNode.DATASET_NAME_INDEX, proteinDataSetSt);
                        if (datasetNameIndexHits.hasNext()) {
                            datasetId = datasetNameIndexHits.getSingle();
                        }
                        if (datasetId < 0) {
                            datasetProperties.put(DatasetNode.NAME_PROPERTY, proteinDataSetSt);
                            datasetId = inserter.createNode(datasetProperties);
                            datasetNameIndex.add(datasetId, MapUtil.map(DatasetNode.DATASET_NAME_INDEX, proteinDataSetSt));
                            //----flushing dataset name index---
                            datasetNameIndex.flush();
                            //---adding dataset node to node_type index----
                            nodeTypeIndex.add(datasetId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, DatasetNode.NODE_TYPE));
                        }
                        inserter.createRelationship(currentProteinId, datasetId, proteinDatasetRel, null);
                        //---------------------------------------------------------------------------------------------


                        if (uniprotDataXML.getCitations()) {
                            importProteinCitations(entryXMLElem,
                                    inserter,
                                    indexProvider,
                                    currentProteinId,
                                    uniprotDataXML);
                        }


                        //-------------------------------keywords------------------------------------------------------
                        if (uniprotDataXML.getKeywords()) {
                            List<Element> keywordsList = entryXMLElem.asJDomElement().getChildren(UniprotStuff.KEYWORD_TAG_NAME);
                            for (Element keywordElem : keywordsList) {
                                String keywordId = keywordElem.getAttributeValue(UniprotStuff.KEYWORD_ID_ATTRIBUTE);
                                String keywordName = keywordElem.getText();
                                long keywordNodeId = -1;
                                IndexHits<Long> keyworIdIndexHits = keywordIdIndex.get(KeywordNode.KEYWORD_ID_INDEX, keywordId);
                                if (keyworIdIndexHits.hasNext()) {
                                    keywordNodeId = keyworIdIndexHits.getSingle();
                                }
                                if (keywordNodeId < 0) {

                                    keywordProperties.put(KeywordNode.ID_PROPERTY, keywordId);
                                    keywordProperties.put(KeywordNode.NAME_PROPERTY, keywordName);

                                    keywordNodeId = inserter.createNode(keywordProperties);

                                    keywordIdIndex.add(keywordNodeId, MapUtil.map(KeywordNode.KEYWORD_ID_INDEX, keywordId));
                                    keywordNameIndex.add(keywordNodeId, MapUtil.map(KeywordNode.KEYWORD_NAME_INDEX, keywordName));

                                    //---flushing keyword id index----
                                    keywordIdIndex.flush();

                                    //---adding keyword node to node_type index----
                                    nodeTypeIndex.add(keywordNodeId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, KeywordNode.NODE_TYPE));
                                }
                                inserter.createRelationship(currentProteinId, keywordNodeId, proteinKeywordRel, null);
                            }
                        }
                        //---------------------------------------------------------------------------------------


                        for (Element dbReferenceElem : dbReferenceList) {

                            //-------------------------------INTERPRO------------------------------------------------------  
                            if (dbReferenceElem.getAttributeValue(UniprotStuff.DB_REFERENCE_TYPE_ATTRIBUTE).equals(UniprotStuff.INTERPRO_DB_REFERENCE_TYPE)) {

                                if (uniprotDataXML.getInterpro()) {
                                    String interproId = dbReferenceElem.getAttributeValue(UniprotStuff.DB_REFERENCE_ID_ATTRIBUTE);
                                    //long interproNodeId = indexService.getSingleNode(InterproNode.INTERPRO_ID_INDEX, interproId);
                                    long interproNodeId = -1;
                                    IndexHits<Long> interproIdIndexHits = interproIdIndex.get(InterproNode.INTERPRO_ID_INDEX, interproId);
                                    if (interproIdIndexHits.hasNext()) {
                                        interproNodeId = interproIdIndexHits.getSingle();
                                    }

                                    if (interproNodeId < 0) {
                                        String interproEntryNameSt = "";
                                        List<Element> properties = dbReferenceElem.getChildren(UniprotStuff.DB_REFERENCE_PROPERTY_TAG_NAME);
                                        for (Element prop : properties) {
                                            if (prop.getAttributeValue(UniprotStuff.DB_REFERENCE_TYPE_ATTRIBUTE).equals(UniprotStuff.INTERPRO_ENTRY_NAME)) {
                                                interproEntryNameSt = prop.getAttributeValue(UniprotStuff.DB_REFERENCE_VALUE_ATTRIBUTE);
                                                break;
                                            }
                                        }

                                        interproProperties.put(InterproNode.ID_PROPERTY, interproId);
                                        interproProperties.put(InterproNode.NAME_PROPERTY, interproEntryNameSt);
                                        interproNodeId = inserter.createNode(interproProperties);

                                        interproIdIndex.add(interproNodeId, MapUtil.map(InterproNode.INTERPRO_ID_INDEX, interproId));
                                        //flushing interpro id index
                                        interproIdIndex.flush();

                                        //---adding interpro node to node_type index----
                                        nodeTypeIndex.add(interproNodeId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, InterproNode.NODE_TYPE));
                                    }

                                    inserter.createRelationship(currentProteinId, interproNodeId, proteinInterproRel, null);
                                }

                            } //-------------------------------PFAM------------------------------------------------------  
                            else if (dbReferenceElem.getAttributeValue(UniprotStuff.DB_REFERENCE_TYPE_ATTRIBUTE).equals("Pfam")) {

                                if (uniprotDataXML.getPfam()) {
                                    String pfamId = dbReferenceElem.getAttributeValue(UniprotStuff.DB_REFERENCE_ID_ATTRIBUTE);
                                    long pfamNodeId = -1;
                                    IndexHits<Long> pfamIdIndexHits = pfamIdIndex.get(PfamNode.PFAM_ID_INDEX, pfamId);
                                    if (pfamIdIndexHits.hasNext()) {
                                        pfamNodeId = pfamIdIndexHits.getSingle();
                                    }

                                    if (pfamNodeId < 0) {
                                        String pfamEntryNameSt = "";
                                        List<Element> properties = dbReferenceElem.getChildren(UniprotStuff.DB_REFERENCE_PROPERTY_TAG_NAME);
                                        for (Element prop : properties) {
                                            if (prop.getAttributeValue(UniprotStuff.DB_REFERENCE_TYPE_ATTRIBUTE).equals("entry name")) {
                                                pfamEntryNameSt = prop.getAttributeValue(UniprotStuff.DB_REFERENCE_VALUE_ATTRIBUTE);
                                                break;
                                            }
                                        }

                                        pfamProperties.put(PfamNode.ID_PROPERTY, pfamId);
                                        pfamProperties.put(PfamNode.NAME_PROPERTY, pfamEntryNameSt);
                                        pfamNodeId = inserter.createNode(pfamProperties);

                                        pfamIdIndex.add(pfamNodeId, MapUtil.map(PfamNode.PFAM_ID_INDEX, pfamId));
                                        //flushing pfam id index
                                        pfamIdIndex.flush();

                                        //---adding pfam node to node_type index----
                                        nodeTypeIndex.add(pfamNodeId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, PfamNode.NODE_TYPE));
                                    }

                                    inserter.createRelationship(currentProteinId, pfamNodeId, proteinPfamRel, null);
                                }


                            } //-------------------GO -----------------------------
                            else if (dbReferenceElem.getAttributeValue(UniprotStuff.DB_REFERENCE_TYPE_ATTRIBUTE).toUpperCase().equals(UniprotStuff.GO_DB_REFERENCE_TYPE)) {

                                if (uniprotDataXML.getGeneOntology()) {
                                    String goId = dbReferenceElem.getAttributeValue(UniprotStuff.DB_REFERENCE_ID_ATTRIBUTE);
                                    String evidenceSt = "";
                                    List<Element> props = dbReferenceElem.getChildren(UniprotStuff.DB_REFERENCE_PROPERTY_TAG_NAME);
                                    for (Element element : props) {
                                        if (element.getAttributeValue(UniprotStuff.DB_REFERENCE_TYPE_ATTRIBUTE).equals(UniprotStuff.EVIDENCE_TYPE_ATTRIBUTE)) {
                                            evidenceSt = element.getAttributeValue("value");
                                            if (evidenceSt == null) {
                                                evidenceSt = "";
                                            }
                                            break;
                                        }
                                    }
                                    long goTermNodeId = goTermIdIndex.get(GoTermNode.GO_TERM_ID_INDEX, goId).getSingle();
                                    proteinGoProperties.put(ProteinGoRel.EVIDENCE_PROPERTY, evidenceSt);
                                    inserter.createRelationship(currentProteinId, goTermNodeId, proteinGoRel, proteinGoProperties);
                                }

                            }

                        }
                        //---------------------------------------------------------------------------------------

                        //---------------------------------------------------------------------------------------
                        //--------------------------------organism-----------------------------------------------

                        String scName, commName, synName;
                        scName = "";
                        commName = "";
                        synName = "";

                        Element organismElem = entryXMLElem.asJDomElement().getChild(UniprotStuff.ORGANISM_TAG_NAME);

                        List<Element> organismNames = organismElem.getChildren(UniprotStuff.ORGANISM_NAME_TAG_NAME);
                        for (Element element : organismNames) {
                            String type = element.getAttributeValue(UniprotStuff.ORGANISM_NAME_TYPE_ATTRIBUTE);
                            switch (type) {
                                case UniprotStuff.ORGANISM_SCIENTIFIC_NAME_TYPE:
                                    scName = element.getText();
                                    break;
                                case UniprotStuff.ORGANISM_COMMON_NAME_TYPE:
                                    commName = element.getText();
                                    break;
                                case UniprotStuff.ORGANISM_SYNONYM_NAME_TYPE:
                                    synName = element.getText();
                                    break;
                            }
                        }

                        //long organismNodeId = indexService.getSingleNode(OrganismNode.ORGANISM_SCIENTIFIC_NAME_INDEX, scName);
                        long organismNodeId = -1;
                        IndexHits<Long> organismScientifiNameIndexHits = organismScientificNameIndex.get(OrganismNode.ORGANISM_SCIENTIFIC_NAME_INDEX, scName);
                        if (organismScientifiNameIndexHits.hasNext()) {
                            organismNodeId = organismScientifiNameIndexHits.getSingle();
                        }
                        if (organismNodeId < 0) {

                            organismProperties.put(OrganismNode.COMMON_NAME_PROPERTY, commName);
                            organismProperties.put(OrganismNode.SCIENTIFIC_NAME_PROPERTY, scName);
                            organismProperties.put(OrganismNode.SYNONYM_NAME_PROPERTY, synName);


                            List<Element> organismDbRefElems = organismElem.getChildren(UniprotStuff.DB_REFERENCE_TAG_NAME);
                            boolean ncbiIdFound = false;
                            if (organismDbRefElems != null) {
                                for (Element dbRefElem : organismDbRefElems) {
                                    String t = dbRefElem.getAttributeValue("type");
                                    if (t.equals("NCBI Taxonomy")) {
                                        organismProperties.put(OrganismNode.NCBI_TAXONOMY_ID_PROPERTY, dbRefElem.getAttributeValue("id"));
                                        ncbiIdFound = true;
                                        break;
                                    }
                                }
                            }
                            if (!ncbiIdFound) {
                                organismProperties.put(OrganismNode.NCBI_TAXONOMY_ID_PROPERTY, "");
                            }
                            organismNodeId = inserter.createNode(organismProperties);

                            organismScientificNameIndex.add(organismNodeId, MapUtil.map(OrganismNode.ORGANISM_SCIENTIFIC_NAME_INDEX, scName));
                            organismNcbiTaxonomyIdIndex.add(organismNodeId, MapUtil.map(OrganismNode.NCBI_TAXONOMY_ID_PROPERTY, organismProperties.get(OrganismNode.NCBI_TAXONOMY_ID_PROPERTY)));

                            //flushing organism scientifica name index
                            organismScientificNameIndex.flush();

                            //---adding organism node to node_type index----
                            nodeTypeIndex.add(organismNodeId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, OrganismNode.NODE_TYPE));

                            Element lineage = entryXMLElem.asJDomElement().getChild("organism").getChild("lineage");
                            List<Element> taxons = lineage.getChildren("taxon");

                            Element firstTaxonElem = taxons.get(0);

                            //long firstTaxonId = indexService.getSingleNode(TaxonNode.TAXON_NAME_INDEX, firstTaxonElem.getText());
                            long firstTaxonId = -1;
                            IndexHits<Long> firstTaxonIndexHits = taxonNameIndex.get(TaxonNode.TAXON_NAME_INDEX, firstTaxonElem.getText());
                            if (firstTaxonIndexHits.hasNext()) {
                                firstTaxonId = firstTaxonIndexHits.getSingle();
                            }

                            if (firstTaxonId < 0) {

                                String firstTaxonName = firstTaxonElem.getText();
                                taxonProperties.put(TaxonNode.NAME_PROPERTY, firstTaxonName);
                                firstTaxonId = createTaxonNode(taxonProperties, inserter, taxonNameIndex, nodeTypeIndex);
                                //flushing taxon name index--
                                taxonNameIndex.flush();

                            }

                            long lastTaxonId = firstTaxonId;
                            for (int i = 1; i < taxons.size(); i++) {
                                String taxonName = taxons.get(i).getText();
                                long currentTaxonId = -1;
                                IndexHits<Long> currentTaxonIndexHits = taxonNameIndex.get(TaxonNode.TAXON_NAME_INDEX, taxonName);
                                if (currentTaxonIndexHits.hasNext()) {
                                    currentTaxonId = currentTaxonIndexHits.getSingle();
                                }
                                if (currentTaxonId < 0) {

                                    taxonProperties.put(TaxonNode.NAME_PROPERTY, taxonName);
                                    currentTaxonId = createTaxonNode(taxonProperties, inserter, taxonNameIndex, nodeTypeIndex);
                                    //flushing taxon name index--
                                    taxonNameIndex.flush();
                                    inserter.createRelationship(lastTaxonId, currentTaxonId, taxonParentRel, null);


                                }
                                lastTaxonId = currentTaxonId;
                            }

                            inserter.createRelationship(lastTaxonId, organismNodeId, taxonParentRel, null);

                        }


                        //---------------------------------------------------------------------------------------
                        //---------------------------------------------------------------------------------------

                        inserter.createRelationship(currentProteinId, organismNodeId, proteinOrganismRel, null);

                        proteinCounter++;
                        if ((proteinCounter % limitForPrintingOut) == 0) {
                            String countProteinsSt = proteinCounter + " proteins inserted!!";
                            logger.log(Level.INFO, countProteinsSt);
                        }

                    }
                }

            } catch (Exception e) {
                logger.log(Level.SEVERE, ("Exception retrieving protein " + currentAccessionId));
                logger.log(Level.SEVERE, e.getMessage());
                StackTraceElement[] trace = e.getStackTrace();
                for (StackTraceElement stackTraceElement : trace) {
                    logger.log(Level.SEVERE, stackTraceElement.toString());
                }
            } finally {

                try {
                    //------closing writers-------
                    enzymeIdsNotFoundBuff.close();

                    // shutdown, makes sure all changes are written to disk
                    indexProvider.shutdown();
                    inserter.shutdown();

                    // closing logger file handler
                    fh.close();

                    //-----------------writing stats file---------------------
                    long elapsedTime = System.nanoTime() - initTime;
                    long elapsedSeconds = Math.round((elapsedTime / 1000000000.0));
                    long hours = elapsedSeconds / 3600;
                    long minutes = (elapsedSeconds % 3600) / 60;
                    long seconds = (elapsedSeconds % 3600) % 60;

                    statsBuff.write("Statistics for program ImportUniprot:\nInput file: " + inFile.getName()
                            + "\nThere were " + proteinCounter + " proteins inserted.\n"
                            + "The elapsed time was: " + hours + "h " + minutes + "m " + seconds + "s\n");

                    //---closing stats writer---
                    statsBuff.close();


                } catch (IOException ex) {
                    Logger.getLogger(ImportUniprot.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    private static void importProteinFeatures(XMLElement entryXMLElem,
            BatchInserter inserter,
            BatchInserterIndexProvider indexProvider,
            long currentProteinId) {

        //-----------------create batch indexes----------------------------------
        //----------------------------------------------------------------------
        BatchInserterIndex featureTypeNameIndex = indexProvider.nodeIndex(FeatureTypeNode.FEATURE_TYPE_NAME_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        BatchInserterIndex nodeTypeIndex = indexProvider.nodeIndex(Bio4jManager.NODE_TYPE_INDEX_NAME,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        //------------------------------------------------------------------------


        //--------------------------------features----------------------------------------------------
        List<Element> featuresList = entryXMLElem.asJDomElement().getChildren(UniprotStuff.FEATURE_TAG_NAME);

        for (Element featureElem : featuresList) {

            String featureTypeSt = featureElem.getAttributeValue(UniprotStuff.FEATURE_TYPE_ATTRIBUTE);
            //long featureTypeNodeId = indexService.getSingleNode(FeatureTypeNode.FEATURE_TYPE_NAME_INDEX, featureTypeSt);
            long featureTypeNodeId = -1;
            IndexHits<Long> featureTypeNameIndexHits = featureTypeNameIndex.get(FeatureTypeNode.FEATURE_TYPE_NAME_INDEX, featureTypeSt);
            if (featureTypeNameIndexHits.hasNext()) {
                featureTypeNodeId = featureTypeNameIndexHits.getSingle();
            }
            featureTypeNameIndexHits.close();

            if (featureTypeNodeId < 0) {

                featureTypeProperties.put(FeatureTypeNode.NAME_PROPERTY, featureTypeSt);
                featureTypeNodeId = inserter.createNode(featureTypeProperties);
                //indexService.index(featureTypeNodeId, FeatureTypeNode.FEATURE_TYPE_NAME_INDEX, featureTypeSt);
                featureTypeNameIndex.add(featureTypeNodeId, MapUtil.map(FeatureTypeNode.FEATURE_TYPE_NAME_INDEX, featureTypeSt));
                //---flushing feature type name index----
                featureTypeNameIndex.flush();

                //---adding feature type node to node_type index----
                nodeTypeIndex.add(featureTypeNodeId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, FeatureTypeNode.NODE_TYPE));

            }

            String featureDescSt = featureElem.getAttributeValue(UniprotStuff.FEATURE_DESCRIPTION_ATTRIBUTE);
            if (featureDescSt == null) {
                featureDescSt = "";
            }
            String featureIdSt = featureElem.getAttributeValue(UniprotStuff.FEATURE_ID_ATTRIBUTE);
            if (featureIdSt == null) {
                featureIdSt = "";
            }
            String featureStatusSt = featureElem.getAttributeValue(UniprotStuff.STATUS_ATTRIBUTE);
            if (featureStatusSt == null) {
                featureStatusSt = "";
            }
            String featureEvidenceSt = featureElem.getAttributeValue(UniprotStuff.EVIDENCE_ATTRIBUTE);
            if (featureEvidenceSt == null) {
                featureEvidenceSt = "";
            }

            Element locationElem = featureElem.getChild(UniprotStuff.FEATURE_LOCATION_TAG_NAME);
            Element positionElem = locationElem.getChild(UniprotStuff.FEATURE_POSITION_TAG_NAME);
            String beginFeatureSt;
            String endFeatureSt;
            if (positionElem != null) {
                beginFeatureSt = positionElem.getAttributeValue(UniprotStuff.FEATURE_POSITION_POSITION_ATTRIBUTE);
                endFeatureSt = beginFeatureSt;
            } else {
                beginFeatureSt = locationElem.getChild(UniprotStuff.FEATURE_LOCATION_BEGIN_TAG_NAME).getAttributeValue(UniprotStuff.FEATURE_LOCATION_POSITION_ATTRIBUTE);
                endFeatureSt = locationElem.getChild(UniprotStuff.FEATURE_LOCATION_END_TAG_NAME).getAttributeValue(UniprotStuff.FEATURE_LOCATION_POSITION_ATTRIBUTE);
            }

            if (beginFeatureSt == null) {
                beginFeatureSt = "";
            }
            if (endFeatureSt == null) {
                endFeatureSt = "";
            }

            String originalSt = featureElem.getChildText(UniprotStuff.FEATURE_ORIGINAL_TAG_NAME);
            String variationSt = featureElem.getChildText(UniprotStuff.FEATURE_VARIATION_TAG_NAME);
            if (originalSt == null) {
                originalSt = "";
            }
            if (variationSt == null) {
                variationSt = "";
            }
            String featureRefSt = featureElem.getAttributeValue(UniprotStuff.FEATURE_REF_ATTRIBUTE);
            if (featureRefSt == null) {
                featureRefSt = "";
            }

            featureProperties.put(BasicFeatureRel.DESCRIPTION_PROPERTY, featureDescSt);
            featureProperties.put(BasicFeatureRel.ID_PROPERTY, featureIdSt);
            featureProperties.put(BasicFeatureRel.EVIDENCE_PROPERTY, featureEvidenceSt);
            featureProperties.put(BasicFeatureRel.STATUS_PROPERTY, featureStatusSt);
            featureProperties.put(BasicFeatureRel.BEGIN_PROPERTY, beginFeatureSt);
            featureProperties.put(BasicFeatureRel.END_PROPERTY, endFeatureSt);
            featureProperties.put(BasicFeatureRel.ORIGINAL_PROPERTY, originalSt);
            featureProperties.put(BasicFeatureRel.VARIATION_PROPERTY, variationSt);
            featureProperties.put(BasicFeatureRel.REF_PROPERTY, featureRefSt);
            switch (featureTypeSt) {
                case ActiveSiteFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, activeSiteFeatureRel, featureProperties);
                    break;
                case BindingSiteFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, bindingSiteFeatureRel, featureProperties);
                    break;
                case CrossLinkFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, crossLinkFeatureRel, featureProperties);
                    break;
                case GlycosylationSiteFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, glycosylationSiteFeatureRel, featureProperties);
                    break;
                case InitiatorMethionineFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, initiatorMethionineFeatureRel, featureProperties);
                    break;
                case LipidMoietyBindingRegionFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, lipidMoietyBindingRegionFeatureRel, featureProperties);
                    break;
                case MetalIonBindingSiteFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, metalIonBindingSiteFeatureRel, featureProperties);
                    break;
                case ModifiedResidueFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, modifiedResidueFeatureRel, featureProperties);
                    break;
                case NonStandardAminoAcidFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, nonStandardAminoAcidFeatureRel, featureProperties);
                    break;
                case NonTerminalResidueFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, nonTerminalResidueFeatureRel, featureProperties);
                    break;
                case PeptideFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, peptideFeatureRel, featureProperties);
                    break;
                case UnsureResidueFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, unsureResidueFeatureRel, featureProperties);
                    break;
                case MutagenesisSiteFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, mutagenesisSiteFeatureRel, featureProperties);
                    break;
                case SequenceVariantFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, sequenceVariantFeatureRel, featureProperties);
                    break;
                case CalciumBindingRegionFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, calciumBindingRegionFeatureRel, featureProperties);
                    break;
                case ChainFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, chainFeatureRel, featureProperties);
                    break;
                case CoiledCoilRegionFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, coiledCoilRegionFeatureRel, featureProperties);
                    break;
                case CompositionallyBiasedRegionFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, compositionallyBiasedRegionFeatureRel, featureProperties);
                    break;
                case DisulfideBondFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, disulfideBondFeatureRel, featureProperties);
                    break;
                case DnaBindingRegionFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, dnaBindingRegionFeatureRel, featureProperties);
                    break;
                case DomainFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, domainFeatureRel, featureProperties);
                    break;
                case HelixFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, helixFeatureRel, featureProperties);
                    break;
                case IntramembraneRegionFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, intramembraneRegionFeatureRel, featureProperties);
                    break;
                case NonConsecutiveResiduesFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, nonConsecutiveResiduesFeatureRel, featureProperties);
                    break;
                case NucleotidePhosphateBindingRegionFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, nucleotidePhosphateBindingRegionFeatureRel, featureProperties);
                    break;
                case PropeptideFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, propeptideFeatureRel, featureProperties);
                    break;
                case RegionOfInterestFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, regionOfInterestFeatureRel, featureProperties);
                    break;
                case RepeatFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, repeatFeatureRel, featureProperties);
                    break;
                case ShortSequenceMotifFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, shortSequenceMotifFeatureRel, featureProperties);
                    break;
                case SignalPeptideFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, signalPeptideFeatureRel, featureProperties);
                    break;
                case SpliceVariantFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, spliceVariantFeatureRel, featureProperties);
                    break;
                case StrandFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, strandFeatureRel, featureProperties);
                    break;
                case TopologicalDomainFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, topologicalDomainFeatureRel, featureProperties);
                    break;
                case TransitPeptideFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, transitPeptideFeatureRel, featureProperties);
                    break;
                case TransmembraneRegionFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, transmembraneRegionFeatureRel, featureProperties);
                    break;
                case ZincFingerRegionFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, zincFingerRegionFeatureRel, featureProperties);
                    break;
                case SiteFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, siteFeatureRel, featureProperties);
                    break;
                case TurnFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, turnFeatureRel, featureProperties);
                    break;
                case SequenceConflictFeatureRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, featureTypeNodeId, sequenceConflictFeatureRel, featureProperties);
                    break;
            }

        }

    }

    private static void importProteinComments(XMLElement entryXMLElem,
            BatchInserter inserter,
            BatchInserterIndexProvider indexProvider,
            long currentProteinId,
            String proteinSequence,
            UniprotDataXML uniprotDataXML) {

        //---------------indexes declaration---------------------------
        BatchInserterIndex commentTypeNameIndex = indexProvider.nodeIndex(CommentTypeNode.COMMENT_TYPE_NAME_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        BatchInserterIndex subcellularLocationNameIndex = indexProvider.nodeIndex(SubcellularLocationNode.SUBCELLULAR_LOCATION_NAME_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        BatchInserterIndex isoformIdIndex = indexProvider.nodeIndex(IsoformNode.ISOFORM_ID_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        BatchInserterIndex nodeTypeIndex = indexProvider.nodeIndex(Bio4jManager.NODE_TYPE_INDEX_NAME,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        //-----------------------------------------------------------

        List<Element> comments = entryXMLElem.asJDomElement().getChildren(UniprotStuff.COMMENT_TAG_NAME);

        for (Element commentElem : comments) {

            String commentTypeSt = commentElem.getAttributeValue(UniprotStuff.COMMENT_TYPE_ATTRIBUTE);

            Element textElem = commentElem.getChild("text");
            String commentTextSt = "";
            String commentStatusSt = "";
            String commentEvidenceSt = "";
            if (textElem != null) {
                commentTextSt = textElem.getText();
                commentStatusSt = textElem.getAttributeValue("status");
                if (commentStatusSt == null) {
                    commentStatusSt = "";
                }
                commentEvidenceSt = textElem.getAttributeValue("evidence");
                if (commentEvidenceSt == null) {
                    commentEvidenceSt = "";
                }
            }

            commentProperties.put(BasicCommentRel.TEXT_PROPERTY, commentTextSt);
            commentProperties.put(BasicCommentRel.STATUS_PROPERTY, commentStatusSt);
            commentProperties.put(BasicCommentRel.EVIDENCE_PROPERTY, commentEvidenceSt);

            //-----------------COMMENT TYPE NODE RETRIEVING/CREATION---------------------- 
            //long commentTypeId = indexService.getSingleNode(CommentTypeNode.COMMENT_TYPE_NAME_INDEX, commentTypeSt);
            IndexHits<Long> commentTypeNameIndexHits = commentTypeNameIndex.get(CommentTypeNode.COMMENT_TYPE_NAME_INDEX, commentTypeSt);
            long commentTypeId = -1;
            if (commentTypeNameIndexHits.hasNext()) {
                commentTypeId = commentTypeNameIndexHits.getSingle();
            }
            commentTypeNameIndexHits.close();
            if (commentTypeId < 0) {
                commentTypeProperties.put(CommentTypeNode.NAME_PROPERTY, commentTypeSt);
                commentTypeId = inserter.createNode(commentTypeProperties);
                commentTypeNameIndex.add(commentTypeId, MapUtil.map(CommentTypeNode.COMMENT_TYPE_NAME_INDEX, commentTypeSt));

                //----flushing the indexation----
                commentTypeNameIndex.flush();

                //---adding comment type node to node_type index----
                nodeTypeIndex.add(commentTypeId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, CommentTypeNode.NODE_TYPE));
            }
            //-----toxic dose----------------
            switch (commentTypeSt) {
                case ToxicDoseCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, toxicDoseCommentRel, commentProperties);
                    break;
                case CautionCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, cautionCommentRel, commentProperties);
                    break;
                case CofactorCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, cofactorCommentRel, commentProperties);
                    break;
                case DiseaseCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, diseaseCommentRel, commentProperties);
                    break;
                case OnlineInformationCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    onlineInformationCommentProperties.put(OnlineInformationCommentRel.STATUS_PROPERTY, commentStatusSt);
                    onlineInformationCommentProperties.put(OnlineInformationCommentRel.EVIDENCE_PROPERTY, commentEvidenceSt);
                    onlineInformationCommentProperties.put(OnlineInformationCommentRel.TEXT_PROPERTY, commentTextSt);
                    String nameSt = commentElem.getAttributeValue("name");
                    if (nameSt == null) {
                        nameSt = "";
                    }
                    String linkSt = "";
                    Element linkElem = commentElem.getChild("link");
                    if (linkElem != null) {
                        String uriSt = linkElem.getAttributeValue("uri");
                        if (uriSt != null) {
                            linkSt = uriSt;
                        }
                    }
                    onlineInformationCommentProperties.put(OnlineInformationCommentRel.NAME_PROPERTY, nameSt);
                    onlineInformationCommentProperties.put(OnlineInformationCommentRel.LINK_PROPERTY, linkSt);
                    inserter.createRelationship(currentProteinId, commentTypeId, onlineInformationCommentRel, onlineInformationCommentProperties);
                    break;
                case TissueSpecificityCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, tissueSpecificityCommentRel, commentProperties);
                    break;
                case FunctionCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, functionCommentRel, commentProperties);
                    break;
                case BiotechnologyCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, biotechnologyCommentRel, commentProperties);
                    break;
                case SubunitCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, subunitCommentRel, commentProperties);
                    break;
                case PolymorphismCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, polymorphismCommentRel, commentProperties);
                    break;
                case DomainCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, domainCommentRel, commentProperties);
                    break;
                case PostTranslationalModificationCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, postTranslationalModificationCommentRel, commentProperties);
                    break;
                case CatalyticActivityCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, catalyticActivityCommentRel, commentProperties);
                    break;
                case DisruptionPhenotypeCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, disruptionPhenotypeCommentRel, commentProperties);
                    break;
                case BioPhysicoChemicalPropertiesCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    biophysicochemicalCommentProperties.put(BioPhysicoChemicalPropertiesCommentRel.STATUS_PROPERTY, commentStatusSt);
                    biophysicochemicalCommentProperties.put(BioPhysicoChemicalPropertiesCommentRel.EVIDENCE_PROPERTY, commentEvidenceSt);
                    biophysicochemicalCommentProperties.put(BioPhysicoChemicalPropertiesCommentRel.TEXT_PROPERTY, commentTextSt);
                    String phDependenceSt = commentElem.getChildText("phDependence");
                    String temperatureDependenceSt = commentElem.getChildText("temperatureDependence");
                    if (phDependenceSt == null) {
                        phDependenceSt = "";
                    }
                    if (temperatureDependenceSt == null) {
                        temperatureDependenceSt = "";
                    }
                    String absorptionMaxSt = "";
                    String absorptionTextSt = "";
                    Element absorptionElem = commentElem.getChild("absorption");
                    if (absorptionElem != null) {
                        absorptionMaxSt = absorptionElem.getChildText("max");
                        absorptionTextSt = absorptionElem.getChildText("text");
                        if (absorptionMaxSt == null) {
                            absorptionMaxSt = "";
                        }
                        if (absorptionTextSt == null) {
                            absorptionTextSt = "";
                        }
                    }
                    String kineticsSt = "";
                    Element kineticsElem = commentElem.getChild("kinetics");
                    if (kineticsElem != null) {
                        kineticsSt = new XMLElement(kineticsElem).toString();
                    }
                    String redoxPotentialSt = "";
                    String redoxPotentialEvidenceSt = "";
                    Element redoxPotentialElem = commentElem.getChild("redoxPotential");
                    if (redoxPotentialElem != null) {
                        redoxPotentialSt = redoxPotentialElem.getText();
                        redoxPotentialEvidenceSt = redoxPotentialElem.getAttributeValue("evidence");
                        if (redoxPotentialSt == null) {
                            redoxPotentialSt = "";
                        }
                        if (redoxPotentialEvidenceSt == null) {
                            redoxPotentialEvidenceSt = "";
                        }
                    }
                    biophysicochemicalCommentProperties.put(BioPhysicoChemicalPropertiesCommentRel.TEMPERATURE_DEPENDENCE_PROPERTY, temperatureDependenceSt);
                    biophysicochemicalCommentProperties.put(BioPhysicoChemicalPropertiesCommentRel.PH_DEPENDENCE_PROPERTY, phDependenceSt);
                    biophysicochemicalCommentProperties.put(BioPhysicoChemicalPropertiesCommentRel.KINETICS_XML_PROPERTY, kineticsSt);
                    biophysicochemicalCommentProperties.put(BioPhysicoChemicalPropertiesCommentRel.ABSORPTION_MAX_PROPERTY, absorptionMaxSt);
                    biophysicochemicalCommentProperties.put(BioPhysicoChemicalPropertiesCommentRel.ABSORPTION_TEXT_PROPERTY, absorptionTextSt);
                    biophysicochemicalCommentProperties.put(BioPhysicoChemicalPropertiesCommentRel.REDOX_POTENTIAL_EVIDENCE_PROPERTY, redoxPotentialEvidenceSt);
                    biophysicochemicalCommentProperties.put(BioPhysicoChemicalPropertiesCommentRel.REDOX_POTENTIAL_PROPERTY, redoxPotentialSt);
                    inserter.createRelationship(currentProteinId, commentTypeId, bioPhysicoChemicalPropertiesCommentRel, biophysicochemicalCommentProperties);
                    break;
                case AllergenCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, allergenCommentRel, commentProperties);
                    break;
                case PathwayCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, pathwayCommentRel, commentProperties);
                    break;
                case InductionCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, inductionCommentRel, commentProperties);
                    break;
                case ProteinSubcellularLocationRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    if (uniprotDataXML.getSubcellularLocations()) {
                        List<Element> subcLocations = commentElem.getChildren(UniprotStuff.SUBCELLULAR_LOCATION_TAG_NAME);

                        for (Element subcLocation : subcLocations) {

                            List<Element> locations = subcLocation.getChildren(UniprotStuff.LOCATION_TAG_NAME);
                            Element firstLocation = locations.get(0);
                            //long firstLocationId = indexService.getSingleNode(SubcellularLocationNode.SUBCELLULAR_LOCATION_NAME_INDEX, firstLocation.getTextTrim());
                            long firstLocationId = -1;
                            IndexHits<Long> firstLocationIndexHits = subcellularLocationNameIndex.get(SubcellularLocationNode.SUBCELLULAR_LOCATION_NAME_INDEX, firstLocation.getTextTrim());
                            if (firstLocationIndexHits.hasNext()) {
                                firstLocationId = firstLocationIndexHits.getSingle();
                            }
                            firstLocationIndexHits.close();
                            long lastLocationId = firstLocationId;

                            if (firstLocationId < 0) {
                                subcellularLocationProperties.put(SubcellularLocationNode.NAME_PROPERTY, firstLocation.getTextTrim());
                                lastLocationId = createSubcellularLocationNode(subcellularLocationProperties, inserter, subcellularLocationNameIndex, nodeTypeIndex);
                                //---flushing subcellular location name index---
                                subcellularLocationNameIndex.flush();
                            }

                            for (int i = 1; i < locations.size(); i++) {

                                long tempLocationId;
                                IndexHits<Long> tempLocationIndexHits = subcellularLocationNameIndex.get(SubcellularLocationNode.SUBCELLULAR_LOCATION_NAME_INDEX, locations.get(i).getTextTrim());
                                if (tempLocationIndexHits.hasNext()) {
                                    tempLocationId = tempLocationIndexHits.getSingle();
                                    tempLocationIndexHits.close();
                                } else {
                                    subcellularLocationProperties.put(SubcellularLocationNode.NAME_PROPERTY, locations.get(i).getTextTrim());
                                    tempLocationId = createSubcellularLocationNode(subcellularLocationProperties, inserter, subcellularLocationNameIndex, nodeTypeIndex);
                                    subcellularLocationNameIndex.flush();
                                }

                                inserter.createRelationship(tempLocationId, lastLocationId, subcellularLocationParentRel, null);
                                lastLocationId = tempLocationId;
                            }
                            Element lastLocation = locations.get(locations.size() - 1);
                            String evidenceSt = lastLocation.getAttributeValue(UniprotStuff.EVIDENCE_ATTRIBUTE);
                            String statusSt = lastLocation.getAttributeValue(UniprotStuff.STATUS_ATTRIBUTE);
                            String topologyStatusSt = "";
                            String topologySt = "";
                            Element topologyElem = subcLocation.getChild("topology");
                            if (topologyElem != null) {
                                topologySt = topologyElem.getText();
                                topologyStatusSt = topologyElem.getAttributeValue("status");
                            }
                            if (topologyStatusSt == null) {
                                topologyStatusSt = "";
                            }
                            if (topologySt == null) {
                                topologySt = "";
                            }
                            if (evidenceSt == null) {
                                evidenceSt = "";
                            }
                            if (statusSt == null) {
                                statusSt = "";
                            }
                            proteinSubcellularLocationProperties.put(ProteinSubcellularLocationRel.EVIDENCE_PROPERTY, evidenceSt);
                            proteinSubcellularLocationProperties.put(ProteinSubcellularLocationRel.STATUS_PROPERTY, statusSt);
                            proteinSubcellularLocationProperties.put(ProteinSubcellularLocationRel.TOPOLOGY_PROPERTY, topologySt);
                            proteinSubcellularLocationProperties.put(ProteinSubcellularLocationRel.TOPOLOGY_STATUS_PROPERTY, topologyStatusSt);
                            inserter.createRelationship(currentProteinId, lastLocationId, proteinSubcellularLocationRel, proteinSubcellularLocationProperties);

                        }
                    }
                    break;
                case UniprotStuff.COMMENT_ALTERNATIVE_PRODUCTS_TYPE:
                    if (uniprotDataXML.getIsoforms()) {
                        List<Element> eventList = commentElem.getChildren("event");
                        List<Element> isoformList = commentElem.getChildren("isoform");

                        for (Element isoformElem : isoformList) {
                            String isoformIdSt = isoformElem.getChildText("id");
                            String isoformNoteSt = isoformElem.getChildText("note");
                            String isoformNameSt = isoformElem.getChildText("name");
                            String isoformSeqSt = "";
                            Element isoSeqElem = isoformElem.getChild("sequence");
                            if (isoSeqElem != null) {
                                String isoSeqTypeSt = isoSeqElem.getAttributeValue("type");
                                if (isoSeqTypeSt.equals("displayed")) {
                                    isoformSeqSt = proteinSequence;
                                }
                            }
                            if (isoformNoteSt == null) {
                                isoformNoteSt = "";
                            }
                            if (isoformNameSt == null) {
                                isoformNameSt = "";
                            }
                            isoformProperties.put(IsoformNode.ID_PROPERTY, isoformIdSt);
                            isoformProperties.put(IsoformNode.NOTE_PROPERTY, isoformNoteSt);
                            isoformProperties.put(IsoformNode.NAME_PROPERTY, isoformNameSt);
                            isoformProperties.put(IsoformNode.SEQUENCE_PROPERTY, isoformSeqSt);
                            //--------------------------------------------------------
                            //long isoformId = indexService.getSingleNode(IsoformNode.ISOFORM_ID_INDEX, isoformIdSt);
                            long isoformId = -1;
                            IndexHits<Long> isoformIdIndexHits = isoformIdIndex.get(IsoformNode.ISOFORM_ID_INDEX, isoformIdSt);
                            if (isoformIdIndexHits.hasNext()) {
                                isoformId = isoformIdIndexHits.getSingle();
                            }
                            isoformIdIndexHits.close();
                            if (isoformId < 0) {
                                isoformId = createIsoformNode(isoformProperties, inserter, isoformIdIndex, nodeTypeIndex);
                            }

                            for (Element eventElem : eventList) {

                                String eventTypeSt = eventElem.getAttributeValue("type");
                                switch (eventTypeSt) {
                                    case AlternativeProductInitiationRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                                        inserter.createRelationship(isoformId, alternativeProductInitiationId, isoformEventGeneratorRel, null);
                                        break;
                                    case AlternativeProductPromoterRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                                        inserter.createRelationship(isoformId, alternativeProductPromoterId, isoformEventGeneratorRel, null);
                                        break;
                                    case AlternativeProductRibosomalFrameshiftingRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                                        inserter.createRelationship(isoformId, alternativeProductRibosomalFrameshiftingId, isoformEventGeneratorRel, null);
                                        break;
                                    case AlternativeProductSplicingRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                                        inserter.createRelationship(isoformId, alternativeProductSplicingId, isoformEventGeneratorRel, null);
                                        break;
                                }
                            }

                            //protein isoform relationship
                            inserter.createRelationship(currentProteinId, isoformId, proteinIsoformRel, null);

                        }
                    }
                    break;
                case UniprotStuff.COMMENT_SEQUENCE_CAUTION_TYPE:
                    sequenceCautionProperties.put(BasicProteinSequenceCautionRel.EVIDENCE_PROPERTY, commentEvidenceSt);
                    sequenceCautionProperties.put(BasicProteinSequenceCautionRel.STATUS_PROPERTY, commentStatusSt);
                    sequenceCautionProperties.put(BasicProteinSequenceCautionRel.TEXT_PROPERTY, commentTextSt);
                    Element conflictElem = commentElem.getChild("conflict");
                    if (conflictElem != null) {

                        String conflictTypeSt = conflictElem.getAttributeValue("type");
                        String resourceSt = "";
                        String idSt = "";
                        String versionSt = "";

                        ArrayList<String> positionsList = new ArrayList<>();

                        Element sequenceElem = conflictElem.getChild("sequence");
                        if (sequenceElem != null) {
                            resourceSt = sequenceElem.getAttributeValue("resource");
                            if (resourceSt == null) {
                                resourceSt = "";
                            }
                            idSt = sequenceElem.getAttributeValue("id");
                            if (idSt == null) {
                                idSt = "";
                            }
                            versionSt = sequenceElem.getAttributeValue("version");
                            if (versionSt == null) {
                                versionSt = "";
                            }
                        }

                        Element locationElem = commentElem.getChild("location");
                        if (locationElem != null) {
                            Element positionElem = locationElem.getChild("position");
                            if (positionElem != null) {
                                String tempPos = positionElem.getAttributeValue("position");
                                if (tempPos != null) {
                                    positionsList.add(tempPos);
                                }
                            }
                        }

                        sequenceCautionProperties.put(BasicProteinSequenceCautionRel.RESOURCE_PROPERTY, resourceSt);
                        sequenceCautionProperties.put(BasicProteinSequenceCautionRel.ID_PROPERTY, idSt);
                        sequenceCautionProperties.put(BasicProteinSequenceCautionRel.VERSION_PROPERTY, versionSt);
                        switch (conflictTypeSt) {
                            case ProteinErroneousGeneModelPredictionRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                                if (positionsList.size() > 0) {
                                    for (String tempPosition : positionsList) {
                                        sequenceCautionProperties.put(BasicProteinSequenceCautionRel.POSITION_PROPERTY, tempPosition);
                                        inserter.createRelationship(currentProteinId, seqCautionErroneousGeneModelPredictionId, proteinErroneousGeneModelPredictionRel, sequenceCautionProperties);
                                    }
                                } else {
                                    sequenceCautionProperties.put(BasicProteinSequenceCautionRel.POSITION_PROPERTY, "");
                                    inserter.createRelationship(currentProteinId, seqCautionErroneousGeneModelPredictionId, proteinErroneousGeneModelPredictionRel, sequenceCautionProperties);
                                }
                                break;
                            case ProteinErroneousInitiationRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                                if (positionsList.size() > 0) {
                                    for (String tempPosition : positionsList) {
                                        sequenceCautionProperties.put(BasicProteinSequenceCautionRel.POSITION_PROPERTY, tempPosition);
                                        inserter.createRelationship(currentProteinId, seqCautionErroneousInitiationId, proteinErroneousInitiationRel, sequenceCautionProperties);
                                    }
                                } else {
                                    sequenceCautionProperties.put(BasicProteinSequenceCautionRel.POSITION_PROPERTY, "");
                                    inserter.createRelationship(currentProteinId, seqCautionErroneousInitiationId, proteinErroneousInitiationRel, sequenceCautionProperties);
                                }
                                break;
                            case ProteinErroneousTranslationRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                                if (positionsList.size() > 0) {
                                    for (String tempPosition : positionsList) {
                                        sequenceCautionProperties.put(BasicProteinSequenceCautionRel.POSITION_PROPERTY, tempPosition);
                                        inserter.createRelationship(currentProteinId, seqCautionErroneousTranslationId, proteinErroneousTranslationRel, sequenceCautionProperties);
                                    }
                                } else {
                                    sequenceCautionProperties.put(BasicProteinSequenceCautionRel.POSITION_PROPERTY, "");
                                    inserter.createRelationship(currentProteinId, seqCautionErroneousTranslationId, proteinErroneousTranslationRel, sequenceCautionProperties);
                                }
                                break;
                            case ProteinErroneousTerminationRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                                if (positionsList.size() > 0) {
                                    for (String tempPosition : positionsList) {
                                        sequenceCautionProperties.put(BasicProteinSequenceCautionRel.POSITION_PROPERTY, tempPosition);
                                        inserter.createRelationship(currentProteinId, seqCautionErroneousTerminationId, proteinErroneousTerminationRel, sequenceCautionProperties);
                                    }
                                } else {
                                    sequenceCautionProperties.put(BasicProteinSequenceCautionRel.POSITION_PROPERTY, "");
                                    inserter.createRelationship(currentProteinId, seqCautionErroneousTerminationId, proteinErroneousTerminationRel, sequenceCautionProperties);
                                }
                                break;
                            case ProteinFrameshiftRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                                if (positionsList.size() > 0) {
                                    for (String tempPosition : positionsList) {
                                        sequenceCautionProperties.put(BasicProteinSequenceCautionRel.POSITION_PROPERTY, tempPosition);
                                        inserter.createRelationship(currentProteinId, seqCautionFrameshiftId, proteinFrameshiftRel, sequenceCautionProperties);
                                    }
                                } else {
                                    sequenceCautionProperties.put(BasicProteinSequenceCautionRel.POSITION_PROPERTY, "");
                                    inserter.createRelationship(currentProteinId, seqCautionFrameshiftId, proteinFrameshiftRel, sequenceCautionProperties);
                                }
                                break;
                            case ProteinMiscellaneousDiscrepancyRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                                if (positionsList.size() > 0) {
                                    for (String tempPosition : positionsList) {
                                        sequenceCautionProperties.put(BasicProteinSequenceCautionRel.POSITION_PROPERTY, tempPosition);
                                        inserter.createRelationship(currentProteinId, seqCautionMiscellaneousDiscrepancyId, proteinMiscellaneousDiscrepancyRel, sequenceCautionProperties);
                                    }
                                } else {
                                    sequenceCautionProperties.put(BasicProteinSequenceCautionRel.POSITION_PROPERTY, "");
                                    inserter.createRelationship(currentProteinId, seqCautionMiscellaneousDiscrepancyId, proteinMiscellaneousDiscrepancyRel, sequenceCautionProperties);
                                }
                                break;
                        }
                    }
                    break;
                case DevelopmentalStageCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, developmentalStageCommentRel, commentProperties);
                    break;
                case MiscellaneousCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, miscellaneousCommentRel, commentProperties);
                    break;
                case SimilarityCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, similarityCommentRel, commentProperties);
                    break;
                case RnaEditingCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    rnaEditingCommentProperties.put(RnaEditingCommentRel.STATUS_PROPERTY, commentStatusSt);
                    rnaEditingCommentProperties.put(RnaEditingCommentRel.EVIDENCE_PROPERTY, commentEvidenceSt);
                    rnaEditingCommentProperties.put(RnaEditingCommentRel.TEXT_PROPERTY, commentTextSt);
                    List<Element> locationsList = commentElem.getChildren("location");
                    for (Element tempLoc : locationsList) {
                        String positionSt = tempLoc.getChild("position").getAttributeValue("position");
                        rnaEditingCommentProperties.put(RnaEditingCommentRel.POSITION_PROPERTY, positionSt);
                        inserter.createRelationship(currentProteinId, commentTypeId, rnaEditingCommentRel, rnaEditingCommentProperties);
                    }
                    break;
                case PharmaceuticalCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, pharmaceuticalCommentRel, commentProperties);
                    break;
                case EnzymeRegulationCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    inserter.createRelationship(currentProteinId, commentTypeId, enzymeRegulationCommentRel, commentProperties);
                    break;
                case MassSpectrometryCommentRel.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                    String methodSt = commentElem.getAttributeValue("method");
                    String massSt = commentElem.getAttributeValue("mass");
                    if (methodSt == null) {
                        methodSt = "";
                    }
                    if (massSt == null) {
                        massSt = "";
                    }
                    String beginSt = "";
                    String endSt = "";
                    Element locationElem = commentElem.getChild("location");
                    if (locationElem != null) {
                        Element beginElem = commentElem.getChild("begin");
                        Element endElem = commentElem.getChild("end");
                        if (beginElem != null) {
                            beginSt = beginElem.getAttributeValue("position");
                        }

                        if (endElem != null) {
                            endSt = endElem.getAttributeValue("position");
                        }
                    }
                    massSpectrometryCommentProperties.put(MassSpectrometryCommentRel.STATUS_PROPERTY, commentStatusSt);
                    massSpectrometryCommentProperties.put(MassSpectrometryCommentRel.EVIDENCE_PROPERTY, commentEvidenceSt);
                    massSpectrometryCommentProperties.put(MassSpectrometryCommentRel.TEXT_PROPERTY, commentTextSt);
                    massSpectrometryCommentProperties.put(MassSpectrometryCommentRel.METHOD_PROPERTY, methodSt);
                    massSpectrometryCommentProperties.put(MassSpectrometryCommentRel.MASS_PROPERTY, massSt);
                    massSpectrometryCommentProperties.put(MassSpectrometryCommentRel.BEGIN_PROPERTY, beginSt);
                    massSpectrometryCommentProperties.put(MassSpectrometryCommentRel.END_PROPERTY, endSt);
                    inserter.createRelationship(currentProteinId, commentTypeId, massSpectrometryCommentRel, massSpectrometryCommentProperties);
                    break;
            }

        }


    }

    private static String getProteinFullName(Element proteinElement) {
        if (proteinElement == null) {
            return "";
        } else {
            Element recElem = proteinElement.getChild(UniprotStuff.PROTEIN_RECOMMENDED_NAME_TAG_NAME);
            if (recElem == null) {
                return "";
            } else {
                return recElem.getChildText(UniprotStuff.PROTEIN_FULL_NAME_TAG_NAME);
            }
        }
    }

    private static String getProteinShortName(Element proteinElement) {
        if (proteinElement == null) {
            return "";
        } else {
            Element recElem = proteinElement.getChild(UniprotStuff.PROTEIN_RECOMMENDED_NAME_TAG_NAME);
            if (recElem == null) {
                return "";
            } else {
                return recElem.getChildText(UniprotStuff.PROTEIN_SHORT_NAME_TAG_NAME);
            }
        }
    }

    private static long createIsoformNode(Map<String, Object> isoformProperties,
            BatchInserter inserter,
            BatchInserterIndex isoformIdIndex,
            BatchInserterIndex nodeTypeIndex) {

        long isoformId = inserter.createNode(isoformProperties);
        isoformIdIndex.add(isoformId, MapUtil.map(IsoformNode.ISOFORM_ID_INDEX, isoformProperties.get(IsoformNode.ID_PROPERTY)));
        //---adding isoform node to node_type index----
        nodeTypeIndex.add(isoformId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, IsoformNode.NODE_TYPE));

        return isoformId;
    }

    private static long createTaxonNode(Map<String, Object> taxonProperties,
            BatchInserter inserter,
            BatchInserterIndex taxonNameIndex,
            BatchInserterIndex nodeTypeIndex) {

        long taxonId = inserter.createNode(taxonProperties);
        taxonNameIndex.add(taxonId, MapUtil.map(TaxonNode.TAXON_NAME_INDEX, taxonProperties.get(TaxonNode.NAME_PROPERTY)));
        //---adding taxon node to node_type index----
        nodeTypeIndex.add(taxonId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, TaxonNode.NODE_TYPE));

        return taxonId;
    }

    private static long createPersonNode(Map<String, Object> personProperties,
            BatchInserter inserter,
            BatchInserterIndex index,
            BatchInserterIndex nodeTypeIndex) {

        long personId = inserter.createNode(personProperties);
        index.add(personId, MapUtil.map(PersonNode.PERSON_NAME_FULL_TEXT_INDEX, personProperties.get(PersonNode.NAME_PROPERTY)));
        //---adding person node to node_type index----
        nodeTypeIndex.add(personId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, PersonNode.NODE_TYPE));

        return personId;
    }

    private static long createConsortiumNode(Map<String, Object> consortiumProperties,
            BatchInserter inserter,
            BatchInserterIndex index,
            BatchInserterIndex nodeTypeIndex) {

        long consortiumId = inserter.createNode(consortiumProperties);
        index.add(consortiumId, MapUtil.map(ConsortiumNode.CONSORTIUM_NAME_INDEX, consortiumProperties.get(ConsortiumNode.NAME_PROPERTY)));
        //---adding consortium node to node_type index----
        nodeTypeIndex.add(consortiumId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, ConsortiumNode.NODE_TYPE));

        return consortiumId;
    }

    private static long createInstituteNode(Map<String, Object> instituteProperties,
            BatchInserter inserter,
            BatchInserterIndex index,
            BatchInserterIndex nodeTypeIndex) {

        long instituteId = inserter.createNode(instituteProperties);
        index.add(instituteId, MapUtil.map(InstituteNode.INSTITUTE_NAME_INDEX, instituteProperties.get(InstituteNode.NAME_PROPERTY)));
        //---adding institute node to node_type index----
        nodeTypeIndex.add(instituteId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, InstituteNode.NODE_TYPE));

        return instituteId;
    }

    private static long createCountryNode(Map<String, Object> countryProperties,
            BatchInserter inserter,
            BatchInserterIndex index,
            BatchInserterIndex nodeTypeIndex) {

        long countryId = inserter.createNode(countryProperties);
        index.add(countryId, MapUtil.map(CountryNode.COUNTRY_NAME_INDEX, countryProperties.get(CountryNode.NAME_PROPERTY)));
        //---adding country node to node_type index----
        nodeTypeIndex.add(countryId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, CountryNode.NODE_TYPE));

        return countryId;
    }

    private static long createCityNode(Map<String, Object> cityProperties,
            BatchInserter inserter,
            BatchInserterIndex index,
            BatchInserterIndex nodeTypeIndex) {

        long cityId = inserter.createNode(cityProperties);
        index.add(cityId, MapUtil.map(CityNode.CITY_NAME_INDEX, cityProperties.get(CityNode.NAME_PROPERTY)));
        //---adding city node to node_type index----
        nodeTypeIndex.add(cityId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, CityNode.NODE_TYPE));

        return cityId;
    }

    private static long createDbNode(Map<String, Object> dbProperties,
            BatchInserter inserter,
            BatchInserterIndex index,
            BatchInserterIndex nodeTypeIndex) {

        long dbId = inserter.createNode(dbProperties);
        index.add(dbId, MapUtil.map(DBNode.DB_NAME_INDEX, dbProperties.get(DBNode.NAME_PROPERTY)));
        //---adding db node to node_type index----
        nodeTypeIndex.add(dbId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, DBNode.NODE_TYPE));

        return dbId;
    }

    private static long createSubcellularLocationNode(Map<String, Object> subcellularLocationProperties,
            BatchInserter inserter,
            BatchInserterIndex index,
            BatchInserterIndex nodeTypeIndex) {

        long subcellularLocationId = inserter.createNode(subcellularLocationProperties);
        index.add(subcellularLocationId, MapUtil.map(SubcellularLocationNode.SUBCELLULAR_LOCATION_NAME_INDEX, subcellularLocationProperties.get(SubcellularLocationNode.NAME_PROPERTY)));
        //---adding subcellular location node to node_type index----
        nodeTypeIndex.add(subcellularLocationId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, SubcellularLocationNode.NODE_TYPE));

        return subcellularLocationId;
    }

    private static void importProteinCitations(XMLElement entryXMLElem,
            BatchInserter inserter,
            BatchInserterIndexProvider indexProvider,
            long currentProteinId,
            UniprotDataXML uniprotDataXML) {

        //-----------------create batch indexes----------------------------------
        //----------------------------------------------------------------------
        BatchInserterIndex personNameIndex = indexProvider.nodeIndex(PersonNode.PERSON_NAME_FULL_TEXT_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, FULL_TEXT_ST));
        BatchInserterIndex consortiumNameIndex = indexProvider.nodeIndex(ConsortiumNode.CONSORTIUM_NAME_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        BatchInserterIndex thesisTitleIndex = indexProvider.nodeIndex(ThesisNode.THESIS_TITLE_FULL_TEXT_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, FULL_TEXT_ST));
        BatchInserterIndex instituteNameIndex = indexProvider.nodeIndex(InstituteNode.INSTITUTE_NAME_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        BatchInserterIndex countryNameIndex = indexProvider.nodeIndex(CountryNode.COUNTRY_NAME_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        BatchInserterIndex cityNameIndex = indexProvider.nodeIndex(CityNode.CITY_NAME_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        BatchInserterIndex patentNumberIndex = indexProvider.nodeIndex(PatentNode.PATENT_NUMBER_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        BatchInserterIndex bookNameIndex = indexProvider.nodeIndex(BookNode.BOOK_NAME_FULL_TEXT_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, FULL_TEXT_ST));
        BatchInserterIndex publisherNameIndex = indexProvider.nodeIndex(PublisherNode.PUBLISHER_NAME_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        BatchInserterIndex onlineArticleTitleIndex = indexProvider.nodeIndex(OnlineArticleNode.ONLINE_ARTICLE_TITLE_FULL_TEXT_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, FULL_TEXT_ST));
        BatchInserterIndex onlineJournalNameIndex = indexProvider.nodeIndex(OnlineJournalNode.ONLINE_JOURNAL_NAME_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        BatchInserterIndex submissionTitleIndex = indexProvider.nodeIndex(SubmissionNode.SUBMISSION_TITLE_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, FULL_TEXT_ST));
        BatchInserterIndex articleTitleIndex = indexProvider.nodeIndex(ArticleNode.ARTICLE_TITLE_FULL_TEXT_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, FULL_TEXT_ST));
        BatchInserterIndex articleDoiIdIndex = indexProvider.nodeIndex(ArticleNode.ARTICLE_DOI_ID_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        BatchInserterIndex articlePubmedIdIndex = indexProvider.nodeIndex(ArticleNode.ARTICLE_PUBMED_ID_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        BatchInserterIndex articleMedlineIdIndex = indexProvider.nodeIndex(ArticleNode.ARTICLE_MEDLINE_ID_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        BatchInserterIndex journalNameIndex = indexProvider.nodeIndex(JournalNode.JOURNAL_NAME_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        BatchInserterIndex nodeTypeIndex = indexProvider.nodeIndex(Bio4jManager.NODE_TYPE_INDEX_NAME,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        BatchInserterIndex dbNameIndex = indexProvider.nodeIndex(DBNode.DB_NAME_INDEX,
                MapUtil.stringMap(PROVIDER_ST, LUCENE_ST, TYPE_ST, EXACT_ST));
        //----------------------------------------------------------------------
        //----------------------------------------------------------------------


        List<Element> referenceList = entryXMLElem.asJDomElement().getChildren(UniprotStuff.REFERENCE_TAG_NAME);

        for (Element reference : referenceList) {
            List<Element> citationsList = reference.getChildren(UniprotStuff.CITATION_TAG_NAME);
            for (Element citation : citationsList) {

                String citationType = citation.getAttributeValue(UniprotStuff.DB_REFERENCE_TYPE_ATTRIBUTE);

                List<Long> authorsPersonNodesIds = new ArrayList<>();
                List<Long> authorsConsortiumNodesIds = new ArrayList<>();

                List<Element> authorPersonElems = citation.getChild("authorList").getChildren("person");
                List<Element> authorConsortiumElems = citation.getChild("authorList").getChildren("consortium");

                for (Element person : authorPersonElems) {
                    //long personId = indexService.getSingleNode(PersonNode.PERSON_NAME_INDEX, person.getAttributeValue("name"));
                    long personId = -1;
                    IndexHits<Long> personNameIndexHits = personNameIndex.get(PersonNode.PERSON_NAME_FULL_TEXT_INDEX, person.getAttributeValue("name"));
                    if (personNameIndexHits.hasNext()) {
                        personId = personNameIndexHits.getSingle();
                    }
                    personNameIndexHits.close();
                    if (personId < 0) {
                        personProperties.put(PersonNode.NAME_PROPERTY, person.getAttributeValue("name"));
                        personId = createPersonNode(personProperties, inserter, personNameIndex, nodeTypeIndex);
                        //flushing person name index
                        personNameIndex.flush();
                    }
                    authorsPersonNodesIds.add(personId);
                }

                for (Element consortium : authorConsortiumElems) {

                    long consortiumId = -1;
                    IndexHits<Long> consortiumIdIndexHits = consortiumNameIndex.get(ConsortiumNode.CONSORTIUM_NAME_INDEX, consortium.getAttributeValue("name"));
                    if (consortiumIdIndexHits.hasNext()) {
                        consortiumId = consortiumIdIndexHits.getSingle();
                    }
                    consortiumIdIndexHits.close();
                    if (consortiumId < 0) {
                        consortiumProperties.put(ConsortiumNode.NAME_PROPERTY, consortium.getAttributeValue("name"));
                        consortiumId = createConsortiumNode(consortiumProperties, inserter, consortiumNameIndex, nodeTypeIndex);
                        //---flushing consortium name index--
                        consortiumNameIndex.flush();
                    }
                    authorsConsortiumNodesIds.add(consortiumId);
                }
                //----------------------------------------------------------------------------
                //-----------------------------THESIS-----------------------------------------
                switch (citationType) {
                    case ThesisNode.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                        if (uniprotDataXML.getThesis()) {
                            String dateSt = citation.getAttributeValue("date");
                            String titleSt = citation.getChildText("title");
                            if (dateSt == null) {
                                dateSt = "";
                            }
                            if (titleSt == null) {
                                titleSt = "";
                            }

                            long thesisId = -1;
                            IndexHits<Long> thesisTitleIndexHits = thesisTitleIndex.get(ThesisNode.THESIS_TITLE_FULL_TEXT_INDEX, titleSt);
                            if (thesisTitleIndexHits.hasNext()) {
                                thesisId = thesisTitleIndexHits.getSingle();
                            }
                            thesisTitleIndexHits.close();
                            if (thesisId < 0) {
                                thesisProperties.put(ThesisNode.DATE_PROPERTY, dateSt);
                                thesisProperties.put(ThesisNode.TITLE_PROPERTY, titleSt);
                                //---thesis node creation and indexing
                                thesisId = inserter.createNode(thesisProperties);
                                nodeTypeIndex.add(thesisId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, ThesisNode.NODE_TYPE));
                                thesisTitleIndex.add(thesisId, MapUtil.map(ThesisNode.THESIS_TITLE_FULL_TEXT_INDEX, titleSt));
                                //flushing thesis title index
                                thesisTitleIndex.flush();
                                //---authors association-----
                                for (long personId : authorsPersonNodesIds) {
                                    inserter.createRelationship(thesisId, personId, thesisAuthorRel, null);
                                }

                                //-----------institute-----------------------------
                                String instituteSt = citation.getAttributeValue("institute");
                                String countrySt = citation.getAttributeValue("country");
                                if (instituteSt != null) {

                                    long instituteId = -1;
                                    IndexHits<Long> instituteNameIndexHits = instituteNameIndex.get(InstituteNode.INSTITUTE_NAME_INDEX, instituteSt);
                                    if (instituteNameIndexHits.hasNext()) {
                                        instituteId = instituteNameIndexHits.getSingle();
                                    }
                                    instituteNameIndexHits.close();
                                    if (instituteId < 0) {
                                        instituteProperties.put(InstituteNode.NAME_PROPERTY, instituteSt);
                                        instituteId = createInstituteNode(instituteProperties, inserter, instituteNameIndex, nodeTypeIndex);
                                        //flushing institute name index
                                        instituteNameIndex.flush();
                                    }
                                    if (countrySt != null) {
                                        //long countryId = indexService.getSingleNode(CountryNode.COUNTRY_NAME_INDEX, countrySt);
                                        long countryId = -1;
                                        IndexHits<Long> countryNameIndexHits = countryNameIndex.get(CountryNode.COUNTRY_NAME_INDEX, countrySt);
                                        if (countryNameIndexHits.hasNext()) {
                                            countryId = countryNameIndexHits.getSingle();
                                        }
                                        countryNameIndexHits.close();
                                        if (countryId < 0) {
                                            countryProperties.put(CountryNode.NAME_PROPERTY, countrySt);
                                            countryId = createCountryNode(countryProperties, inserter, countryNameIndex, nodeTypeIndex);
                                            //flushing country name index
                                            countryNameIndex.flush();
                                        }
                                        inserter.createRelationship(instituteId, countryId, instituteCountryRel, null);
                                    }
                                    inserter.createRelationship(thesisId, instituteId, thesisInstituteRel, null);
                                }
                            }

                            //--protein citation relationship
                            inserter.createRelationship(thesisId, currentProteinId, thesisProteinCitationRel, null);

                        }

                        //----------------------------------------------------------------------------
                        //-----------------------------PATENT-----------------------------------------
                        break;
                    case PatentNode.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                        if (uniprotDataXML.getPatents()) {
                            String numberSt = citation.getAttributeValue("number");
                            String dateSt = citation.getAttributeValue("date");
                            String titleSt = citation.getChildText("title");
                            if (dateSt == null) {
                                dateSt = "";
                            }
                            if (titleSt == null) {
                                titleSt = "";
                            }
                            if (numberSt == null) {
                                numberSt = "";
                            }

                            if (!numberSt.equals("")) {
                                long patentId = -1;
                                IndexHits<Long> patentNumberIndexHits = patentNumberIndex.get(PatentNode.PATENT_NUMBER_INDEX, numberSt);
                                if (patentNumberIndexHits.hasNext()) {
                                    patentId = patentNumberIndexHits.getSingle();
                                }
                                patentNumberIndexHits.close();

                                if (patentId < 0) {
                                    patentProperties.put(PatentNode.NUMBER_PROPERTY, numberSt);
                                    patentProperties.put(PatentNode.DATE_PROPERTY, dateSt);
                                    patentProperties.put(PatentNode.TITLE_PROPERTY, titleSt);
                                    //---patent node creation and indexing
                                    patentId = inserter.createNode(patentProperties);
                                    patentNumberIndex.add(patentId, MapUtil.map(PatentNode.PATENT_NUMBER_INDEX, numberSt));
                                    nodeTypeIndex.add(patentId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, PatentNode.NODE_TYPE));
                                    //---flushing patent number index---
                                    patentNumberIndex.flush();
                                    //---authors association-----
                                    for (long personId : authorsPersonNodesIds) {
                                        inserter.createRelationship(patentId, personId, patentAuthorRel, null);
                                    }
                                }

                                //--protein citation relationship
                                inserter.createRelationship(patentId, currentProteinId, patentProteinCitationRel, null);
                            }
                        }

                        //----------------------------------------------------------------------------
                        //-----------------------------SUBMISSION-----------------------------------------
                        break;
                    case SubmissionNode.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                        if (uniprotDataXML.getSubmissions()) {
                            String dateSt = citation.getAttributeValue("date");
                            String titleSt = citation.getChildText("title");
                            String dbSt = citation.getAttributeValue("db");
                            if (dateSt == null) {
                                dateSt = "";
                            }
                            if (titleSt == null) {
                                titleSt = "";
                            }

                            submissionProperties.put(SubmissionNode.DATE_PROPERTY, dateSt);
                            submissionProperties.put(SubmissionNode.TITLE_PROPERTY, titleSt);

                            long submissionId;
                            IndexHits<Long> submissionTitleIndexHits = submissionTitleIndex.get(SubmissionNode.SUBMISSION_TITLE_INDEX, titleSt);
                            if (submissionTitleIndexHits.hasNext()) {
                                submissionId = submissionTitleIndexHits.getSingle();
                                submissionTitleIndexHits.close();
                            } else {
                                //---submission node creation and indexing
                                submissionId = inserter.createNode(submissionProperties);
                                //--indexing node by type---
                                nodeTypeIndex.add(submissionId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, SubmissionNode.NODE_TYPE));
                                if (!titleSt.isEmpty()) {
                                    //--indexing node by title---
                                    submissionTitleIndex.add(submissionId, MapUtil.map(SubmissionNode.SUBMISSION_TITLE_INDEX, titleSt));
                                    submissionTitleIndex.flush();
                                }
                            }

                            //---authors association-----
                            for (long personId : authorsPersonNodesIds) {
                                inserter.createRelationship(submissionId, personId, submissionAuthorRel, null);
                            }
                            //---authors consortium association-----
                            for (long consortiumId : authorsConsortiumNodesIds) {
                                inserter.createRelationship(submissionId, consortiumId, submissionAuthorRel, null);
                            }

                            if (dbSt != null) {
                                long dbId = -1;
                                IndexHits<Long> dbNameIndexHits = dbNameIndex.get(DBNode.DB_NAME_INDEX, dbSt);
                                if (dbNameIndexHits.hasNext()) {
                                    dbId = dbNameIndexHits.getSingle();
                                }
                                dbNameIndexHits.close();
                                if (dbId < 0) {
                                    dbProperties.put(DBNode.NODE_TYPE_PROPERTY, DBNode.NODE_TYPE);
                                    dbProperties.put(DBNode.NAME_PROPERTY, dbSt);
                                    dbId = createDbNode(dbProperties, inserter, dbNameIndex, nodeTypeIndex);
                                    dbNameIndex.flush();
                                }
                                //-----submission db relationship-----
                                inserter.createRelationship(submissionId, dbId, submissionDbRel, null);
                            }

                            //--protein citation relationship
                            inserter.createRelationship(submissionId, currentProteinId, submissionProteinCitationRel, null);

                        }

                        //----------------------------------------------------------------------------
                        //-----------------------------BOOK-----------------------------------------
                        break;
                    case BookNode.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                        if (uniprotDataXML.getBooks()) {
                            String nameSt = citation.getAttributeValue("name");
                            String dateSt = citation.getAttributeValue("date");
                            String titleSt = citation.getChildText("title");
                            String publisherSt = citation.getAttributeValue("publisher");
                            String firstSt = citation.getAttributeValue("first");
                            String lastSt = citation.getAttributeValue("last");
                            String citySt = citation.getAttributeValue("city");
                            String volumeSt = citation.getAttributeValue("volume");
                            if (nameSt == null) {
                                nameSt = "";
                            }
                            if (dateSt == null) {
                                dateSt = "";
                            }
                            if (titleSt == null) {
                                titleSt = "";
                            }
                            if (publisherSt == null) {
                                publisherSt = "";
                            }
                            if (firstSt == null) {
                                firstSt = "";
                            }
                            if (lastSt == null) {
                                lastSt = "";
                            }
                            if (citySt == null) {
                                citySt = "";
                            }
                            if (volumeSt == null) {
                                volumeSt = "";
                            }

                            long bookId = -1;
                            IndexHits<Long> bookNameIndexHits = bookNameIndex.get(BookNode.BOOK_NAME_FULL_TEXT_INDEX, nameSt);
                            if (bookNameIndexHits.hasNext()) {
                                bookId = bookNameIndexHits.getSingle();
                            }
                            bookNameIndexHits.close();

                            if (bookId < 0) {
                                bookProperties.put(BookNode.NAME_PROPERTY, nameSt);
                                bookProperties.put(BookNode.DATE_PROPERTY, dateSt);
                                //---book node creation and indexing
                                bookId = inserter.createNode(bookProperties);

                                bookNameIndex.add(bookId, MapUtil.map(BookNode.BOOK_NAME_FULL_TEXT_INDEX, nameSt));
                                //--indexing node by type---
                                nodeTypeIndex.add(bookId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, BookNode.NODE_TYPE));

                                //--flushing book name index---
                                bookNameIndex.flush();
                                //---authors association-----
                                for (long personId : authorsPersonNodesIds) {
                                    inserter.createRelationship(bookId, personId, bookAuthorRel, null);
                                }

                                //---editor association-----
                                Element editorListElem = citation.getChild("editorList");
                                if (editorListElem != null) {
                                    List<Element> editorsElems = editorListElem.getChildren("person");
                                    for (Element person : editorsElems) {
                                        //long editorId = indexService.getSingleNode(PersonNode.PERSON_NAME_INDEX, person.getAttributeValue("name"));
                                        long editorId = -1;
                                        IndexHits<Long> personNameIndexHits = personNameIndex.get(PersonNode.PERSON_NAME_FULL_TEXT_INDEX, person.getAttributeValue("name"));
                                        if (personNameIndexHits.hasNext()) {
                                            editorId = personNameIndexHits.getSingle();
                                        }
                                        personNameIndexHits.close();
                                        if (editorId < 0) {
                                            personProperties.put(PersonNode.NAME_PROPERTY, person.getAttributeValue("name"));
                                            editorId = createPersonNode(personProperties, inserter, personNameIndex, nodeTypeIndex);
                                        }
                                        //---flushing person name index---
                                        personNameIndex.flush();
                                        //editor association
                                        inserter.createRelationship(bookId, editorId, bookEditorRel, null);
                                    }
                                }


                                //----publisher--
                                if (!publisherSt.equals("")) {
                                    //long publisherId = indexService.getSingleNode(PublisherNode.PUBLISHER_NAME_INDEX, publisherSt);
                                    long publisherId = -1;
                                    IndexHits<Long> publisherNameIndexHits = publisherNameIndex.get(PublisherNode.PUBLISHER_NAME_INDEX, publisherSt);
                                    if (publisherNameIndexHits.hasNext()) {
                                        publisherId = publisherNameIndexHits.getSingle();
                                    }
                                    publisherNameIndexHits.close();
                                    if (publisherId < 0) {
                                        publisherProperties.put(PublisherNode.NAME_PROPERTY, publisherSt);
                                        publisherId = inserter.createNode(publisherProperties);
                                        //--indexing node by type---
                                        nodeTypeIndex.add(publisherId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, PublisherNode.NODE_TYPE));
                                        publisherNameIndex.add(publisherId, MapUtil.map(PublisherNode.PUBLISHER_NAME_INDEX, publisherSt));
                                        //--flushing publisher name index--
                                        publisherNameIndex.flush();
                                    }
                                    inserter.createRelationship(bookId, publisherId, bookPublisherRel, null);
                                }

                                //-----city-----
                                if (!citySt.equals("")) {
                                    //long cityId = indexService.getSingleNode(CityNode.CITY_NAME_INDEX, citySt);
                                    long cityId = -1;
                                    IndexHits<Long> cityNameIndexHits = cityNameIndex.get(CityNode.CITY_NAME_INDEX, citySt);
                                    if (cityNameIndexHits.hasNext()) {
                                        cityId = cityNameIndexHits.getSingle();
                                    }
                                    cityNameIndexHits.close();
                                    if (cityId < 0) {
                                        cityProperties.put(CityNode.NAME_PROPERTY, citySt);
                                        cityId = createCityNode(cityProperties, inserter, cityNameIndex, nodeTypeIndex);
                                        //-----flushing city name index---
                                        cityNameIndex.flush();
                                    }
                                    inserter.createRelationship(bookId, cityId, bookCityRel, null);
                                }
                            }

                            bookProteinCitationProperties.put(BookProteinCitationRel.FIRST_PROPERTY, firstSt);
                            bookProteinCitationProperties.put(BookProteinCitationRel.LAST_PROPERTY, lastSt);
                            bookProteinCitationProperties.put(BookProteinCitationRel.VOLUME_PROPERTY, volumeSt);
                            bookProteinCitationProperties.put(BookProteinCitationRel.TITLE_PROPERTY, titleSt);
                            //--protein citation relationship
                            inserter.createRelationship(bookId, currentProteinId, bookProteinCitationRel, bookProteinCitationProperties);

                        }

                        //----------------------------------------------------------------------------
                        //-----------------------------ONLINE ARTICLE-----------------------------------------
                        break;
                    case OnlineArticleNode.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                        if (uniprotDataXML.getOnlineArticles()) {
                            String locatorSt = citation.getChildText("locator");
                            String nameSt = citation.getAttributeValue("name");
                            String titleSt = citation.getChildText("title");

                            if (titleSt == null) {
                                titleSt = "";
                            }
                            if (nameSt == null) {
                                nameSt = "";
                            }
                            if (locatorSt == null) {
                                locatorSt = "";
                            }

                            long onlineArticleId = -1;
                            IndexHits<Long> onlineArticleTitleIndexHits = onlineArticleTitleIndex.get(OnlineArticleNode.ONLINE_ARTICLE_TITLE_FULL_TEXT_INDEX, titleSt);
                            if (onlineArticleTitleIndexHits.hasNext()) {
                                onlineArticleId = onlineArticleTitleIndexHits.getSingle();
                            }
                            onlineArticleTitleIndexHits.close();
                            if (onlineArticleId < 0) {
                                onlineArticleProperties.put(OnlineArticleNode.TITLE_PROPERTY, titleSt);
                                onlineArticleId = inserter.createNode(onlineArticleProperties);
                                //--indexing node by type---
                                nodeTypeIndex.add(onlineArticleId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, OnlineArticleNode.NODE_TYPE));

                                if (!titleSt.equals("")) {
                                    onlineArticleTitleIndex.add(onlineArticleId, MapUtil.map(OnlineArticleNode.ONLINE_ARTICLE_TITLE_FULL_TEXT_INDEX, titleSt));
                                    //-----flushing online article title index---
                                    onlineArticleTitleIndex.flush();
                                }

                                //---authors person association-----
                                for (long personId : authorsPersonNodesIds) {
                                    inserter.createRelationship(onlineArticleId, personId, onlineArticleAuthorRel, null);
                                }
                                //---authors consortium association-----
                                for (long consortiumId : authorsConsortiumNodesIds) {
                                    inserter.createRelationship(onlineArticleId, consortiumId, onlineArticleAuthorRel, null);
                                }

                                //------online journal-----------
                                if (!nameSt.equals("")) {

                                    long onlineJournalId = -1;
                                    IndexHits<Long> onlineJournalNameIndexHits = onlineJournalNameIndex.get(OnlineJournalNode.ONLINE_JOURNAL_NAME_INDEX, nameSt);
                                    if (onlineJournalNameIndexHits.hasNext()) {
                                        onlineJournalId = onlineJournalNameIndexHits.getSingle();
                                    }
                                    onlineJournalNameIndexHits.close();
                                    if (onlineJournalId < 0) {
                                        onlineJournalProperties.put(OnlineJournalNode.NAME_PROPERTY, nameSt);
                                        onlineJournalId = inserter.createNode(onlineJournalProperties);
                                        //--indexing node by type---
                                        nodeTypeIndex.add(onlineJournalId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, OnlineJournalNode.NODE_TYPE));
                                        onlineJournalNameIndex.add(onlineJournalId, MapUtil.map(OnlineJournalNode.ONLINE_JOURNAL_NAME_INDEX, nameSt));

                                        //---flushing online journal name index---
                                        onlineJournalNameIndex.flush();
                                    }

                                    onlineArticleJournalProperties.put(OnlineArticleJournalRel.LOCATOR_PROPERTY, locatorSt);
                                    inserter.createRelationship(onlineArticleId, onlineJournalId, onlineArticleJournalRel, onlineArticleJournalProperties);
                                }
                                //----------------------------
                            }
                            //protein citation
                            inserter.createRelationship(onlineArticleId, currentProteinId, onlineArticleProteinCitationRel, null);

                        }

                        //----------------------------------------------------------------------------
                        //-----------------------------ARTICLE-----------------------------------------
                        break;
                    case ArticleNode.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                        if (uniprotDataXML.getArticles()) {
                            String journalNameSt = citation.getAttributeValue("name");
                            String dateSt = citation.getAttributeValue("date");
                            String titleSt = citation.getChildText("title");
                            String firstSt = citation.getAttributeValue("first");
                            String lastSt = citation.getAttributeValue("last");
                            String volumeSt = citation.getAttributeValue("volume");
                            String doiSt = "";
                            String medlineSt = "";
                            String pubmedSt = "";

                            if (journalNameSt == null) {
                                journalNameSt = "";
                            }
                            if (dateSt == null) {
                                dateSt = "";
                            }
                            if (firstSt == null) {
                                firstSt = "";
                            }
                            if (lastSt == null) {
                                lastSt = "";
                            }
                            if (volumeSt == null) {
                                volumeSt = "";
                            }
                            if (titleSt == null) {
                                titleSt = "";
                            }

                            List<Element> dbReferences = citation.getChildren("dbReference");
                            for (Element tempDbRef : dbReferences) {
                                switch (tempDbRef.getAttributeValue("type")) {
                                    case "DOI":
                                        doiSt = tempDbRef.getAttributeValue("id");
                                        break;
                                    case "MEDLINE":
                                        medlineSt = tempDbRef.getAttributeValue("id");
                                        break;
                                    case "PubMed":
                                        pubmedSt = tempDbRef.getAttributeValue("id");
                                        break;
                                }
                            }

                            //long articleId = indexService.getSingleNode(ArticleNode.ARTICLE_TITLE_FULL_TEXT_INDEX, titleSt);
                            long articleId = -1;
                            IndexHits<Long> articleTitleIndexHits = articleTitleIndex.get(ArticleNode.ARTICLE_TITLE_FULL_TEXT_INDEX, titleSt);
                            if (articleTitleIndexHits.hasNext()) {
                                articleId = articleTitleIndexHits.getSingle();
                            }
                            articleTitleIndexHits.close();
                            if (articleId < 0) {
                                articleProperties.put(ArticleNode.TITLE_PROPERTY, titleSt);
                                articleProperties.put(ArticleNode.DOI_ID_PROPERTY, doiSt);
                                articleProperties.put(ArticleNode.MEDLINE_ID_PROPERTY, medlineSt);
                                articleProperties.put(ArticleNode.PUBMED_ID_PROPERTY, pubmedSt);
                                articleId = inserter.createNode(articleProperties);
                                //--indexing node by type---
                                nodeTypeIndex.add(articleId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, ArticleNode.NODE_TYPE));
                                if (!titleSt.equals("")) {
                                    articleTitleIndex.add(articleId, MapUtil.map(ArticleNode.ARTICLE_TITLE_FULL_TEXT_INDEX, titleSt));
                                    //--flushing article title index---
                                    articleTitleIndex.flush();
                                }

                                //---indexing by medline, doi and pubmed--
                                if (!doiSt.isEmpty()) {
                                    articleDoiIdIndex.add(articleId, MapUtil.map(ArticleNode.ARTICLE_DOI_ID_INDEX, doiSt));
                                }
                                if (!medlineSt.isEmpty()) {
                                    articleMedlineIdIndex.add(articleId, MapUtil.map(ArticleNode.ARTICLE_MEDLINE_ID_INDEX, medlineSt));
                                }
                                if (!pubmedSt.isEmpty()) {
                                    articlePubmedIdIndex.add(articleId, MapUtil.map(ArticleNode.ARTICLE_PUBMED_ID_INDEX, pubmedSt));
                                }

                                //---authors person association-----
                                for (long personId : authorsPersonNodesIds) {
                                    inserter.createRelationship(articleId, personId, articleAuthorRel, null);
                                }
                                //---authors consortium association-----
                                for (long consortiumId : authorsConsortiumNodesIds) {
                                    inserter.createRelationship(articleId, consortiumId, articleAuthorRel, null);
                                }

                                //------journal-----------
                                if (!journalNameSt.equals("")) {
                                    //long journalId = indexService.getSingleNode(JournalNode.JOURNAL_NAME_INDEX, journalNameSt);
                                    long journalId = -1;
                                    IndexHits<Long> journalNameIndexHits = journalNameIndex.get(JournalNode.JOURNAL_NAME_INDEX, journalNameSt);
                                    if (journalNameIndexHits.hasNext()) {
                                        journalId = journalNameIndexHits.getSingle();
                                    }
                                    journalNameIndexHits.close();
                                    if (journalId < 0) {
                                        journalProperties.put(JournalNode.NAME_PROPERTY, journalNameSt);
                                        journalId = inserter.createNode(journalProperties);
                                        //--indexing node by type---
                                        nodeTypeIndex.add(journalId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, JournalNode.NODE_TYPE));
                                        journalNameIndex.add(journalId, MapUtil.map(JournalNode.JOURNAL_NAME_INDEX, journalNameSt));
                                        //----flushing journal name index----
                                        journalNameIndex.flush();
                                    }

                                    articleJournalProperties.put(ArticleJournalRel.DATE_PROPERTY, dateSt);
                                    articleJournalProperties.put(ArticleJournalRel.FIRST_PROPERTY, firstSt);
                                    articleJournalProperties.put(ArticleJournalRel.LAST_PROPERTY, lastSt);
                                    articleJournalProperties.put(ArticleJournalRel.VOLUME_PROPERTY, volumeSt);
                                    inserter.createRelationship(articleId, journalId, articleJournalRel, articleJournalProperties);
                                }
                                //----------------------------
                            }
                            //protein citation
                            inserter.createRelationship(articleId, currentProteinId, articleProteinCitationRel, null);

                        }

                        //----------------------------------------------------------------------------
                        //----------------------UNPUBLISHED OBSERVATIONS-----------------------------------------
                        break;
                    case UnpublishedObservationNode.UNIPROT_ATTRIBUTE_TYPE_VALUE:
                        if (uniprotDataXML.getUnpublishedObservations()) {
                            String dateSt = citation.getAttributeValue("date");
                            if (dateSt == null) {
                                dateSt = "";
                            }

                            unpublishedObservationProperties.put(UnpublishedObservationNode.DATE_PROPERTY, dateSt);
                            long unpublishedObservationId = inserter.createNode(unpublishedObservationProperties);
                            //--indexing node by type---
                            nodeTypeIndex.add(unpublishedObservationId, MapUtil.map(Bio4jManager.NODE_TYPE_INDEX_NAME, UnpublishedObservationNode.NODE_TYPE));

                            //---authors person association-----
                            for (long personId : authorsPersonNodesIds) {
                                inserter.createRelationship(unpublishedObservationId, personId, unpublishedObservationAuthorRel, null);
                            }

                            inserter.createRelationship(unpublishedObservationId, currentProteinId, unpublishedObservationProteinCitationRel, null);
                        }
                        break;
                }
            }
        }


    }

    private static String[] convertToStringArray(List<String> list) {
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }
}

```


------

### Index

+ src
  + main
    + java
      + com
        + bio4j
          + neo4j
            + [BasicEntity.java][main/java/com/bio4j/neo4j/BasicEntity.java]
            + [BasicRelationship.java][main/java/com/bio4j/neo4j/BasicRelationship.java]
            + codesamples
              + [BiodieselProductionSample.java][main/java/com/bio4j/neo4j/codesamples/BiodieselProductionSample.java]
              + [GetEnzymeData.java][main/java/com/bio4j/neo4j/codesamples/GetEnzymeData.java]
              + [GetGenesInfo.java][main/java/com/bio4j/neo4j/codesamples/GetGenesInfo.java]
              + [GetGOAnnotationsForOrganism.java][main/java/com/bio4j/neo4j/codesamples/GetGOAnnotationsForOrganism.java]
              + [GetProteinsWithInterpro.java][main/java/com/bio4j/neo4j/codesamples/GetProteinsWithInterpro.java]
              + [RealUseCase1.java][main/java/com/bio4j/neo4j/codesamples/RealUseCase1.java]
              + [RetrieveProteinSample.java][main/java/com/bio4j/neo4j/codesamples/RetrieveProteinSample.java]
            + model
              + nodes
                + [AlternativeProductNode.java][main/java/com/bio4j/neo4j/model/nodes/AlternativeProductNode.java]
                + citation
                  + [ArticleNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/ArticleNode.java]
                  + [BookNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/BookNode.java]
                  + [DBNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/DBNode.java]
                  + [JournalNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/JournalNode.java]
                  + [OnlineArticleNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/OnlineArticleNode.java]
                  + [OnlineJournalNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/OnlineJournalNode.java]
                  + [PatentNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/PatentNode.java]
                  + [PublisherNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/PublisherNode.java]
                  + [SubmissionNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/SubmissionNode.java]
                  + [ThesisNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/ThesisNode.java]
                  + [UnpublishedObservationNode.java][main/java/com/bio4j/neo4j/model/nodes/citation/UnpublishedObservationNode.java]
                + [CityNode.java][main/java/com/bio4j/neo4j/model/nodes/CityNode.java]
                + [CommentTypeNode.java][main/java/com/bio4j/neo4j/model/nodes/CommentTypeNode.java]
                + [ConsortiumNode.java][main/java/com/bio4j/neo4j/model/nodes/ConsortiumNode.java]
                + [CountryNode.java][main/java/com/bio4j/neo4j/model/nodes/CountryNode.java]
                + [DatasetNode.java][main/java/com/bio4j/neo4j/model/nodes/DatasetNode.java]
                + [EnzymeNode.java][main/java/com/bio4j/neo4j/model/nodes/EnzymeNode.java]
                + [FeatureTypeNode.java][main/java/com/bio4j/neo4j/model/nodes/FeatureTypeNode.java]
                + [GoTermNode.java][main/java/com/bio4j/neo4j/model/nodes/GoTermNode.java]
                + [InstituteNode.java][main/java/com/bio4j/neo4j/model/nodes/InstituteNode.java]
                + [InterproNode.java][main/java/com/bio4j/neo4j/model/nodes/InterproNode.java]
                + [IsoformNode.java][main/java/com/bio4j/neo4j/model/nodes/IsoformNode.java]
                + [KeywordNode.java][main/java/com/bio4j/neo4j/model/nodes/KeywordNode.java]
                + ncbi
                  + [NCBITaxonNode.java][main/java/com/bio4j/neo4j/model/nodes/ncbi/NCBITaxonNode.java]
                + [OrganismNode.java][main/java/com/bio4j/neo4j/model/nodes/OrganismNode.java]
                + [PersonNode.java][main/java/com/bio4j/neo4j/model/nodes/PersonNode.java]
                + [PfamNode.java][main/java/com/bio4j/neo4j/model/nodes/PfamNode.java]
                + [ProteinNode.java][main/java/com/bio4j/neo4j/model/nodes/ProteinNode.java]
                + reactome
                  + [ReactomeTermNode.java][main/java/com/bio4j/neo4j/model/nodes/reactome/ReactomeTermNode.java]
                + refseq
                  + [CDSNode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/CDSNode.java]
                  + [GeneNode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/GeneNode.java]
                  + [GenomeElementNode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/GenomeElementNode.java]
                  + rna
                    + [MiscRNANode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/rna/MiscRNANode.java]
                    + [MRNANode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/rna/MRNANode.java]
                    + [NcRNANode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/rna/NcRNANode.java]
                    + [RNANode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/rna/RNANode.java]
                    + [RRNANode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/rna/RRNANode.java]
                    + [TmRNANode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/rna/TmRNANode.java]
                    + [TRNANode.java][main/java/com/bio4j/neo4j/model/nodes/refseq/rna/TRNANode.java]
                + [SequenceCautionNode.java][main/java/com/bio4j/neo4j/model/nodes/SequenceCautionNode.java]
                + [SubcellularLocationNode.java][main/java/com/bio4j/neo4j/model/nodes/SubcellularLocationNode.java]
                + [TaxonNode.java][main/java/com/bio4j/neo4j/model/nodes/TaxonNode.java]
              + relationships
                + aproducts
                  + [AlternativeProductInitiationRel.java][main/java/com/bio4j/neo4j/model/relationships/aproducts/AlternativeProductInitiationRel.java]
                  + [AlternativeProductPromoterRel.java][main/java/com/bio4j/neo4j/model/relationships/aproducts/AlternativeProductPromoterRel.java]
                  + [AlternativeProductRibosomalFrameshiftingRel.java][main/java/com/bio4j/neo4j/model/relationships/aproducts/AlternativeProductRibosomalFrameshiftingRel.java]
                  + [AlternativeProductSplicingRel.java][main/java/com/bio4j/neo4j/model/relationships/aproducts/AlternativeProductSplicingRel.java]
                + citation
                  + article
                    + [ArticleAuthorRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/article/ArticleAuthorRel.java]
                    + [ArticleJournalRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/article/ArticleJournalRel.java]
                    + [ArticleProteinCitationRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/article/ArticleProteinCitationRel.java]
                  + book
                    + [BookAuthorRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/book/BookAuthorRel.java]
                    + [BookCityRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/book/BookCityRel.java]
                    + [BookEditorRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/book/BookEditorRel.java]
                    + [BookProteinCitationRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/book/BookProteinCitationRel.java]
                    + [BookPublisherRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/book/BookPublisherRel.java]
                  + onarticle
                    + [OnlineArticleAuthorRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/onarticle/OnlineArticleAuthorRel.java]
                    + [OnlineArticleJournalRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/onarticle/OnlineArticleJournalRel.java]
                    + [OnlineArticleProteinCitationRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/onarticle/OnlineArticleProteinCitationRel.java]
                  + patent
                    + [PatentAuthorRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/patent/PatentAuthorRel.java]
                    + [PatentProteinCitationRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/patent/PatentProteinCitationRel.java]
                  + submission
                    + [SubmissionAuthorRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/submission/SubmissionAuthorRel.java]
                    + [SubmissionDbRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/submission/SubmissionDbRel.java]
                    + [SubmissionProteinCitationRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/submission/SubmissionProteinCitationRel.java]
                  + thesis
                    + [ThesisAuthorRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/thesis/ThesisAuthorRel.java]
                    + [ThesisInstituteRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/thesis/ThesisInstituteRel.java]
                    + [ThesisProteinCitationRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/thesis/ThesisProteinCitationRel.java]
                  + uo
                    + [UnpublishedObservationAuthorRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/uo/UnpublishedObservationAuthorRel.java]
                    + [UnpublishedObservationProteinCitationRel.java][main/java/com/bio4j/neo4j/model/relationships/citation/uo/UnpublishedObservationProteinCitationRel.java]
                + comment
                  + [AllergenCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/AllergenCommentRel.java]
                  + [BasicCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/BasicCommentRel.java]
                  + [BioPhysicoChemicalPropertiesCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/BioPhysicoChemicalPropertiesCommentRel.java]
                  + [BiotechnologyCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/BiotechnologyCommentRel.java]
                  + [CatalyticActivityCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/CatalyticActivityCommentRel.java]
                  + [CautionCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/CautionCommentRel.java]
                  + [CofactorCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/CofactorCommentRel.java]
                  + [DevelopmentalStageCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/DevelopmentalStageCommentRel.java]
                  + [DiseaseCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/DiseaseCommentRel.java]
                  + [DisruptionPhenotypeCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/DisruptionPhenotypeCommentRel.java]
                  + [DomainCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/DomainCommentRel.java]
                  + [EnzymeRegulationCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/EnzymeRegulationCommentRel.java]
                  + [FunctionCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/FunctionCommentRel.java]
                  + [InductionCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/InductionCommentRel.java]
                  + [MassSpectrometryCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/MassSpectrometryCommentRel.java]
                  + [MiscellaneousCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/MiscellaneousCommentRel.java]
                  + [OnlineInformationCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/OnlineInformationCommentRel.java]
                  + [PathwayCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/PathwayCommentRel.java]
                  + [PharmaceuticalCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/PharmaceuticalCommentRel.java]
                  + [PolymorphismCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/PolymorphismCommentRel.java]
                  + [PostTranslationalModificationCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/PostTranslationalModificationCommentRel.java]
                  + [RnaEditingCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/RnaEditingCommentRel.java]
                  + [SimilarityCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/SimilarityCommentRel.java]
                  + [SubunitCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/SubunitCommentRel.java]
                  + [TissueSpecificityCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/TissueSpecificityCommentRel.java]
                  + [ToxicDoseCommentRel.java][main/java/com/bio4j/neo4j/model/relationships/comment/ToxicDoseCommentRel.java]
                + features
                  + [ActiveSiteFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/ActiveSiteFeatureRel.java]
                  + [BasicFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/BasicFeatureRel.java]
                  + [BindingSiteFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/BindingSiteFeatureRel.java]
                  + [CalciumBindingRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/CalciumBindingRegionFeatureRel.java]
                  + [ChainFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/ChainFeatureRel.java]
                  + [CoiledCoilRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/CoiledCoilRegionFeatureRel.java]
                  + [CompositionallyBiasedRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/CompositionallyBiasedRegionFeatureRel.java]
                  + [CrossLinkFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/CrossLinkFeatureRel.java]
                  + [DisulfideBondFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/DisulfideBondFeatureRel.java]
                  + [DnaBindingRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/DnaBindingRegionFeatureRel.java]
                  + [DomainFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/DomainFeatureRel.java]
                  + [GlycosylationSiteFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/GlycosylationSiteFeatureRel.java]
                  + [HelixFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/HelixFeatureRel.java]
                  + [InitiatorMethionineFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/InitiatorMethionineFeatureRel.java]
                  + [IntramembraneRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/IntramembraneRegionFeatureRel.java]
                  + [LipidMoietyBindingRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/LipidMoietyBindingRegionFeatureRel.java]
                  + [MetalIonBindingSiteFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/MetalIonBindingSiteFeatureRel.java]
                  + [ModifiedResidueFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/ModifiedResidueFeatureRel.java]
                  + [MutagenesisSiteFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/MutagenesisSiteFeatureRel.java]
                  + [NonConsecutiveResiduesFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/NonConsecutiveResiduesFeatureRel.java]
                  + [NonStandardAminoAcidFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/NonStandardAminoAcidFeatureRel.java]
                  + [NonTerminalResidueFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/NonTerminalResidueFeatureRel.java]
                  + [NucleotidePhosphateBindingRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/NucleotidePhosphateBindingRegionFeatureRel.java]
                  + [PeptideFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/PeptideFeatureRel.java]
                  + [PropeptideFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/PropeptideFeatureRel.java]
                  + [RegionOfInterestFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/RegionOfInterestFeatureRel.java]
                  + [RepeatFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/RepeatFeatureRel.java]
                  + [SequenceConflictFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/SequenceConflictFeatureRel.java]
                  + [SequenceVariantFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/SequenceVariantFeatureRel.java]
                  + [ShortSequenceMotifFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/ShortSequenceMotifFeatureRel.java]
                  + [SignalPeptideFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/SignalPeptideFeatureRel.java]
                  + [SiteFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/SiteFeatureRel.java]
                  + [SpliceVariantFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/SpliceVariantFeatureRel.java]
                  + [StrandFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/StrandFeatureRel.java]
                  + [TopologicalDomainFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/TopologicalDomainFeatureRel.java]
                  + [TransitPeptideFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/TransitPeptideFeatureRel.java]
                  + [TransmembraneRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/TransmembraneRegionFeatureRel.java]
                  + [TurnFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/TurnFeatureRel.java]
                  + [UnsureResidueFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/UnsureResidueFeatureRel.java]
                  + [ZincFingerRegionFeatureRel.java][main/java/com/bio4j/neo4j/model/relationships/features/ZincFingerRegionFeatureRel.java]
                + go
                  + [BiologicalProcessRel.java][main/java/com/bio4j/neo4j/model/relationships/go/BiologicalProcessRel.java]
                  + [CellularComponentRel.java][main/java/com/bio4j/neo4j/model/relationships/go/CellularComponentRel.java]
                  + [HasPartOfGoRel.java][main/java/com/bio4j/neo4j/model/relationships/go/HasPartOfGoRel.java]
                  + [IsAGoRel.java][main/java/com/bio4j/neo4j/model/relationships/go/IsAGoRel.java]
                  + [MainGoRel.java][main/java/com/bio4j/neo4j/model/relationships/go/MainGoRel.java]
                  + [MolecularFunctionRel.java][main/java/com/bio4j/neo4j/model/relationships/go/MolecularFunctionRel.java]
                  + [NegativelyRegulatesGoRel.java][main/java/com/bio4j/neo4j/model/relationships/go/NegativelyRegulatesGoRel.java]
                  + [PartOfGoRel.java][main/java/com/bio4j/neo4j/model/relationships/go/PartOfGoRel.java]
                  + [PositivelyRegulatesGoRel.java][main/java/com/bio4j/neo4j/model/relationships/go/PositivelyRegulatesGoRel.java]
                  + [RegulatesGoRel.java][main/java/com/bio4j/neo4j/model/relationships/go/RegulatesGoRel.java]
                + [InstituteCountryRel.java][main/java/com/bio4j/neo4j/model/relationships/InstituteCountryRel.java]
                + [IsoformEventGeneratorRel.java][main/java/com/bio4j/neo4j/model/relationships/IsoformEventGeneratorRel.java]
                + ncbi
                  + [NCBIMainTaxonRel.java][main/java/com/bio4j/neo4j/model/relationships/ncbi/NCBIMainTaxonRel.java]
                  + [NCBITaxonParentRel.java][main/java/com/bio4j/neo4j/model/relationships/ncbi/NCBITaxonParentRel.java]
                  + [NCBITaxonRel.java][main/java/com/bio4j/neo4j/model/relationships/ncbi/NCBITaxonRel.java]
                + protein
                  + [BasicProteinSequenceCautionRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/BasicProteinSequenceCautionRel.java]
                  + [ProteinDatasetRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinDatasetRel.java]
                  + [ProteinEnzymaticActivityRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinEnzymaticActivityRel.java]
                  + [ProteinErroneousGeneModelPredictionRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinErroneousGeneModelPredictionRel.java]
                  + [ProteinErroneousInitiationRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinErroneousInitiationRel.java]
                  + [ProteinErroneousTerminationRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinErroneousTerminationRel.java]
                  + [ProteinErroneousTranslationRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinErroneousTranslationRel.java]
                  + [ProteinFrameshiftRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinFrameshiftRel.java]
                  + [ProteinGenomeElementRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinGenomeElementRel.java]
                  + [ProteinGoRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinGoRel.java]
                  + [ProteinInterproRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinInterproRel.java]
                  + [ProteinIsoformInteractionRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinIsoformInteractionRel.java]
                  + [ProteinIsoformRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinIsoformRel.java]
                  + [ProteinKeywordRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinKeywordRel.java]
                  + [ProteinMiscellaneousDiscrepancyRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinMiscellaneousDiscrepancyRel.java]
                  + [ProteinOrganismRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinOrganismRel.java]
                  + [ProteinPfamRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinPfamRel.java]
                  + [ProteinProteinInteractionRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinProteinInteractionRel.java]
                  + [ProteinReactomeRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinReactomeRel.java]
                  + [ProteinSubcellularLocationRel.java][main/java/com/bio4j/neo4j/model/relationships/protein/ProteinSubcellularLocationRel.java]
                + refseq
                  + [GenomeElementCDSRel.java][main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementCDSRel.java]
                  + [GenomeElementGeneRel.java][main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementGeneRel.java]
                  + [GenomeElementMiscRnaRel.java][main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementMiscRnaRel.java]
                  + [GenomeElementMRnaRel.java][main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementMRnaRel.java]
                  + [GenomeElementNcRnaRel.java][main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementNcRnaRel.java]
                  + [GenomeElementRRnaRel.java][main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementRRnaRel.java]
                  + [GenomeElementTmRnaRel.java][main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementTmRnaRel.java]
                  + [GenomeElementTRnaRel.java][main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementTRnaRel.java]
                + sc
                  + [ErroneousGeneModelPredictionRel.java][main/java/com/bio4j/neo4j/model/relationships/sc/ErroneousGeneModelPredictionRel.java]
                  + [ErroneousInitiationRel.java][main/java/com/bio4j/neo4j/model/relationships/sc/ErroneousInitiationRel.java]
                  + [ErroneousTerminationRel.java][main/java/com/bio4j/neo4j/model/relationships/sc/ErroneousTerminationRel.java]
                  + [ErroneousTranslationRel.java][main/java/com/bio4j/neo4j/model/relationships/sc/ErroneousTranslationRel.java]
                  + [FrameshiftRel.java][main/java/com/bio4j/neo4j/model/relationships/sc/FrameshiftRel.java]
                  + [MiscellaneousDiscrepancyRel.java][main/java/com/bio4j/neo4j/model/relationships/sc/MiscellaneousDiscrepancyRel.java]
                + [SubcellularLocationParentRel.java][main/java/com/bio4j/neo4j/model/relationships/SubcellularLocationParentRel.java]
                + [TaxonParentRel.java][main/java/com/bio4j/neo4j/model/relationships/TaxonParentRel.java]
                + uniref
                  + [UniRef100MemberRel.java][main/java/com/bio4j/neo4j/model/relationships/uniref/UniRef100MemberRel.java]
                  + [UniRef50MemberRel.java][main/java/com/bio4j/neo4j/model/relationships/uniref/UniRef50MemberRel.java]
                  + [UniRef90MemberRel.java][main/java/com/bio4j/neo4j/model/relationships/uniref/UniRef90MemberRel.java]
              + util
                + [Bio4jManager.java][main/java/com/bio4j/neo4j/model/util/Bio4jManager.java]
                + [GoUtil.java][main/java/com/bio4j/neo4j/model/util/GoUtil.java]
                + [NodeIndexer.java][main/java/com/bio4j/neo4j/model/util/NodeIndexer.java]
                + [NodeRetriever.java][main/java/com/bio4j/neo4j/model/util/NodeRetriever.java]
                + [UniprotStuff.java][main/java/com/bio4j/neo4j/model/util/UniprotStuff.java]
            + [Neo4jManager.java][main/java/com/bio4j/neo4j/Neo4jManager.java]
            + programs
              + [GetProteinData.java][main/java/com/bio4j/neo4j/programs/GetProteinData.java]
              + [ImportEnzymeDB.java][main/java/com/bio4j/neo4j/programs/ImportEnzymeDB.java]
              + [ImportGeneOntology.java][main/java/com/bio4j/neo4j/programs/ImportGeneOntology.java]
              + [ImportIsoformSequences.java][main/java/com/bio4j/neo4j/programs/ImportIsoformSequences.java]
              + [ImportNCBITaxonomy.java][main/java/com/bio4j/neo4j/programs/ImportNCBITaxonomy.java]
              + [ImportNeo4jDB.java][main/java/com/bio4j/neo4j/programs/ImportNeo4jDB.java]
              + [ImportProteinInteractions.java][main/java/com/bio4j/neo4j/programs/ImportProteinInteractions.java]
              + [ImportRefSeq.java][main/java/com/bio4j/neo4j/programs/ImportRefSeq.java]
              + [ImportUniprot.java][main/java/com/bio4j/neo4j/programs/ImportUniprot.java]
              + [ImportUniref.java][main/java/com/bio4j/neo4j/programs/ImportUniref.java]
              + [IndexNCBITaxonomyByGiId.java][main/java/com/bio4j/neo4j/programs/IndexNCBITaxonomyByGiId.java]
              + [InitBio4jDB.java][main/java/com/bio4j/neo4j/programs/InitBio4jDB.java]
              + [UploadRefSeqSequencesToS3.java][main/java/com/bio4j/neo4j/programs/UploadRefSeqSequencesToS3.java]
        + ohnosequences
          + util
            + [Executable.java][main/java/com/ohnosequences/util/Executable.java]
            + [ExecuteFromFile.java][main/java/com/ohnosequences/util/ExecuteFromFile.java]
            + genbank
              + [GBCommon.java][main/java/com/ohnosequences/util/genbank/GBCommon.java]
          + xml
            + api
              + interfaces
                + [IAttribute.java][main/java/com/ohnosequences/xml/api/interfaces/IAttribute.java]
                + [IElement.java][main/java/com/ohnosequences/xml/api/interfaces/IElement.java]
                + [INameSpace.java][main/java/com/ohnosequences/xml/api/interfaces/INameSpace.java]
                + [IXmlThing.java][main/java/com/ohnosequences/xml/api/interfaces/IXmlThing.java]
                + [package-info.java][main/java/com/ohnosequences/xml/api/interfaces/package-info.java]
              + model
                + [NameSpace.java][main/java/com/ohnosequences/xml/api/model/NameSpace.java]
                + [package-info.java][main/java/com/ohnosequences/xml/api/model/package-info.java]
                + [XMLAttribute.java][main/java/com/ohnosequences/xml/api/model/XMLAttribute.java]
                + [XMLElement.java][main/java/com/ohnosequences/xml/api/model/XMLElement.java]
                + [XMLElementException.java][main/java/com/ohnosequences/xml/api/model/XMLElementException.java]
              + util
                + [XMLUtil.java][main/java/com/ohnosequences/xml/api/util/XMLUtil.java]
            + model
              + bio4j
                + [Bio4jNodeIndexXML.java][main/java/com/ohnosequences/xml/model/bio4j/Bio4jNodeIndexXML.java]
                + [Bio4jNodeXML.java][main/java/com/ohnosequences/xml/model/bio4j/Bio4jNodeXML.java]
                + [Bio4jPropertyXML.java][main/java/com/ohnosequences/xml/model/bio4j/Bio4jPropertyXML.java]
                + [Bio4jRelationshipIndexXML.java][main/java/com/ohnosequences/xml/model/bio4j/Bio4jRelationshipIndexXML.java]
                + [Bio4jRelationshipXML.java][main/java/com/ohnosequences/xml/model/bio4j/Bio4jRelationshipXML.java]
                + [UniprotDataXML.java][main/java/com/ohnosequences/xml/model/bio4j/UniprotDataXML.java]
              + go
                + [GoAnnotationXML.java][main/java/com/ohnosequences/xml/model/go/GoAnnotationXML.java]
                + [GOSlimXML.java][main/java/com/ohnosequences/xml/model/go/GOSlimXML.java]
                + [GoTermXML.java][main/java/com/ohnosequences/xml/model/go/GoTermXML.java]
                + [SlimSetXML.java][main/java/com/ohnosequences/xml/model/go/SlimSetXML.java]
              + uniprot
                + [ArticleXML.java][main/java/com/ohnosequences/xml/model/uniprot/ArticleXML.java]
                + [CommentXML.java][main/java/com/ohnosequences/xml/model/uniprot/CommentXML.java]
                + [FeatureXML.java][main/java/com/ohnosequences/xml/model/uniprot/FeatureXML.java]
                + [InterproXML.java][main/java/com/ohnosequences/xml/model/uniprot/InterproXML.java]
                + [IsoformXML.java][main/java/com/ohnosequences/xml/model/uniprot/IsoformXML.java]
                + [KeywordXML.java][main/java/com/ohnosequences/xml/model/uniprot/KeywordXML.java]
                + [ProteinXML.java][main/java/com/ohnosequences/xml/model/uniprot/ProteinXML.java]
                + [SubcellularLocationXML.java][main/java/com/ohnosequences/xml/model/uniprot/SubcellularLocationXML.java]
              + util
                + [Argument.java][main/java/com/ohnosequences/xml/model/util/Argument.java]
                + [Arguments.java][main/java/com/ohnosequences/xml/model/util/Arguments.java]
                + [Error.java][main/java/com/ohnosequences/xml/model/util/Error.java]
                + [Execution.java][main/java/com/ohnosequences/xml/model/util/Execution.java]
                + [FlexXMLWrapperClassCreator.java][main/java/com/ohnosequences/xml/model/util/FlexXMLWrapperClassCreator.java]
                + [ScheduledExecutions.java][main/java/com/ohnosequences/xml/model/util/ScheduledExecutions.java]
                + [XMLWrapperClass.java][main/java/com/ohnosequences/xml/model/util/XMLWrapperClass.java]
                + [XMLWrapperClassCreator.java][main/java/com/ohnosequences/xml/model/util/XMLWrapperClassCreator.java]

[main/java/com/bio4j/neo4j/BasicEntity.java]: ../BasicEntity.java.md
[main/java/com/bio4j/neo4j/BasicRelationship.java]: ../BasicRelationship.java.md
[main/java/com/bio4j/neo4j/codesamples/BiodieselProductionSample.java]: ../codesamples/BiodieselProductionSample.java.md
[main/java/com/bio4j/neo4j/codesamples/GetEnzymeData.java]: ../codesamples/GetEnzymeData.java.md
[main/java/com/bio4j/neo4j/codesamples/GetGenesInfo.java]: ../codesamples/GetGenesInfo.java.md
[main/java/com/bio4j/neo4j/codesamples/GetGOAnnotationsForOrganism.java]: ../codesamples/GetGOAnnotationsForOrganism.java.md
[main/java/com/bio4j/neo4j/codesamples/GetProteinsWithInterpro.java]: ../codesamples/GetProteinsWithInterpro.java.md
[main/java/com/bio4j/neo4j/codesamples/RealUseCase1.java]: ../codesamples/RealUseCase1.java.md
[main/java/com/bio4j/neo4j/codesamples/RetrieveProteinSample.java]: ../codesamples/RetrieveProteinSample.java.md
[main/java/com/bio4j/neo4j/model/nodes/AlternativeProductNode.java]: ../model/nodes/AlternativeProductNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/ArticleNode.java]: ../model/nodes/citation/ArticleNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/BookNode.java]: ../model/nodes/citation/BookNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/DBNode.java]: ../model/nodes/citation/DBNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/JournalNode.java]: ../model/nodes/citation/JournalNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/OnlineArticleNode.java]: ../model/nodes/citation/OnlineArticleNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/OnlineJournalNode.java]: ../model/nodes/citation/OnlineJournalNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/PatentNode.java]: ../model/nodes/citation/PatentNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/PublisherNode.java]: ../model/nodes/citation/PublisherNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/SubmissionNode.java]: ../model/nodes/citation/SubmissionNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/ThesisNode.java]: ../model/nodes/citation/ThesisNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/citation/UnpublishedObservationNode.java]: ../model/nodes/citation/UnpublishedObservationNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/CityNode.java]: ../model/nodes/CityNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/CommentTypeNode.java]: ../model/nodes/CommentTypeNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/ConsortiumNode.java]: ../model/nodes/ConsortiumNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/CountryNode.java]: ../model/nodes/CountryNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/DatasetNode.java]: ../model/nodes/DatasetNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/EnzymeNode.java]: ../model/nodes/EnzymeNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/FeatureTypeNode.java]: ../model/nodes/FeatureTypeNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/GoTermNode.java]: ../model/nodes/GoTermNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/InstituteNode.java]: ../model/nodes/InstituteNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/InterproNode.java]: ../model/nodes/InterproNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/IsoformNode.java]: ../model/nodes/IsoformNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/KeywordNode.java]: ../model/nodes/KeywordNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/ncbi/NCBITaxonNode.java]: ../model/nodes/ncbi/NCBITaxonNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/OrganismNode.java]: ../model/nodes/OrganismNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/PersonNode.java]: ../model/nodes/PersonNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/PfamNode.java]: ../model/nodes/PfamNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/ProteinNode.java]: ../model/nodes/ProteinNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/reactome/ReactomeTermNode.java]: ../model/nodes/reactome/ReactomeTermNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/CDSNode.java]: ../model/nodes/refseq/CDSNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/GeneNode.java]: ../model/nodes/refseq/GeneNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/GenomeElementNode.java]: ../model/nodes/refseq/GenomeElementNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/MiscRNANode.java]: ../model/nodes/refseq/rna/MiscRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/MRNANode.java]: ../model/nodes/refseq/rna/MRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/NcRNANode.java]: ../model/nodes/refseq/rna/NcRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/RNANode.java]: ../model/nodes/refseq/rna/RNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/RRNANode.java]: ../model/nodes/refseq/rna/RRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/TmRNANode.java]: ../model/nodes/refseq/rna/TmRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/refseq/rna/TRNANode.java]: ../model/nodes/refseq/rna/TRNANode.java.md
[main/java/com/bio4j/neo4j/model/nodes/SequenceCautionNode.java]: ../model/nodes/SequenceCautionNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/SubcellularLocationNode.java]: ../model/nodes/SubcellularLocationNode.java.md
[main/java/com/bio4j/neo4j/model/nodes/TaxonNode.java]: ../model/nodes/TaxonNode.java.md
[main/java/com/bio4j/neo4j/model/relationships/aproducts/AlternativeProductInitiationRel.java]: ../model/relationships/aproducts/AlternativeProductInitiationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/aproducts/AlternativeProductPromoterRel.java]: ../model/relationships/aproducts/AlternativeProductPromoterRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/aproducts/AlternativeProductRibosomalFrameshiftingRel.java]: ../model/relationships/aproducts/AlternativeProductRibosomalFrameshiftingRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/aproducts/AlternativeProductSplicingRel.java]: ../model/relationships/aproducts/AlternativeProductSplicingRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/article/ArticleAuthorRel.java]: ../model/relationships/citation/article/ArticleAuthorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/article/ArticleJournalRel.java]: ../model/relationships/citation/article/ArticleJournalRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/article/ArticleProteinCitationRel.java]: ../model/relationships/citation/article/ArticleProteinCitationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/book/BookAuthorRel.java]: ../model/relationships/citation/book/BookAuthorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/book/BookCityRel.java]: ../model/relationships/citation/book/BookCityRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/book/BookEditorRel.java]: ../model/relationships/citation/book/BookEditorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/book/BookProteinCitationRel.java]: ../model/relationships/citation/book/BookProteinCitationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/book/BookPublisherRel.java]: ../model/relationships/citation/book/BookPublisherRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/onarticle/OnlineArticleAuthorRel.java]: ../model/relationships/citation/onarticle/OnlineArticleAuthorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/onarticle/OnlineArticleJournalRel.java]: ../model/relationships/citation/onarticle/OnlineArticleJournalRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/onarticle/OnlineArticleProteinCitationRel.java]: ../model/relationships/citation/onarticle/OnlineArticleProteinCitationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/patent/PatentAuthorRel.java]: ../model/relationships/citation/patent/PatentAuthorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/patent/PatentProteinCitationRel.java]: ../model/relationships/citation/patent/PatentProteinCitationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/submission/SubmissionAuthorRel.java]: ../model/relationships/citation/submission/SubmissionAuthorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/submission/SubmissionDbRel.java]: ../model/relationships/citation/submission/SubmissionDbRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/submission/SubmissionProteinCitationRel.java]: ../model/relationships/citation/submission/SubmissionProteinCitationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/thesis/ThesisAuthorRel.java]: ../model/relationships/citation/thesis/ThesisAuthorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/thesis/ThesisInstituteRel.java]: ../model/relationships/citation/thesis/ThesisInstituteRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/thesis/ThesisProteinCitationRel.java]: ../model/relationships/citation/thesis/ThesisProteinCitationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/uo/UnpublishedObservationAuthorRel.java]: ../model/relationships/citation/uo/UnpublishedObservationAuthorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/citation/uo/UnpublishedObservationProteinCitationRel.java]: ../model/relationships/citation/uo/UnpublishedObservationProteinCitationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/AllergenCommentRel.java]: ../model/relationships/comment/AllergenCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/BasicCommentRel.java]: ../model/relationships/comment/BasicCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/BioPhysicoChemicalPropertiesCommentRel.java]: ../model/relationships/comment/BioPhysicoChemicalPropertiesCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/BiotechnologyCommentRel.java]: ../model/relationships/comment/BiotechnologyCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/CatalyticActivityCommentRel.java]: ../model/relationships/comment/CatalyticActivityCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/CautionCommentRel.java]: ../model/relationships/comment/CautionCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/CofactorCommentRel.java]: ../model/relationships/comment/CofactorCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/DevelopmentalStageCommentRel.java]: ../model/relationships/comment/DevelopmentalStageCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/DiseaseCommentRel.java]: ../model/relationships/comment/DiseaseCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/DisruptionPhenotypeCommentRel.java]: ../model/relationships/comment/DisruptionPhenotypeCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/DomainCommentRel.java]: ../model/relationships/comment/DomainCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/EnzymeRegulationCommentRel.java]: ../model/relationships/comment/EnzymeRegulationCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/FunctionCommentRel.java]: ../model/relationships/comment/FunctionCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/InductionCommentRel.java]: ../model/relationships/comment/InductionCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/MassSpectrometryCommentRel.java]: ../model/relationships/comment/MassSpectrometryCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/MiscellaneousCommentRel.java]: ../model/relationships/comment/MiscellaneousCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/OnlineInformationCommentRel.java]: ../model/relationships/comment/OnlineInformationCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/PathwayCommentRel.java]: ../model/relationships/comment/PathwayCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/PharmaceuticalCommentRel.java]: ../model/relationships/comment/PharmaceuticalCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/PolymorphismCommentRel.java]: ../model/relationships/comment/PolymorphismCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/PostTranslationalModificationCommentRel.java]: ../model/relationships/comment/PostTranslationalModificationCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/RnaEditingCommentRel.java]: ../model/relationships/comment/RnaEditingCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/SimilarityCommentRel.java]: ../model/relationships/comment/SimilarityCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/SubunitCommentRel.java]: ../model/relationships/comment/SubunitCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/TissueSpecificityCommentRel.java]: ../model/relationships/comment/TissueSpecificityCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/comment/ToxicDoseCommentRel.java]: ../model/relationships/comment/ToxicDoseCommentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/ActiveSiteFeatureRel.java]: ../model/relationships/features/ActiveSiteFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/BasicFeatureRel.java]: ../model/relationships/features/BasicFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/BindingSiteFeatureRel.java]: ../model/relationships/features/BindingSiteFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/CalciumBindingRegionFeatureRel.java]: ../model/relationships/features/CalciumBindingRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/ChainFeatureRel.java]: ../model/relationships/features/ChainFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/CoiledCoilRegionFeatureRel.java]: ../model/relationships/features/CoiledCoilRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/CompositionallyBiasedRegionFeatureRel.java]: ../model/relationships/features/CompositionallyBiasedRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/CrossLinkFeatureRel.java]: ../model/relationships/features/CrossLinkFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/DisulfideBondFeatureRel.java]: ../model/relationships/features/DisulfideBondFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/DnaBindingRegionFeatureRel.java]: ../model/relationships/features/DnaBindingRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/DomainFeatureRel.java]: ../model/relationships/features/DomainFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/GlycosylationSiteFeatureRel.java]: ../model/relationships/features/GlycosylationSiteFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/HelixFeatureRel.java]: ../model/relationships/features/HelixFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/InitiatorMethionineFeatureRel.java]: ../model/relationships/features/InitiatorMethionineFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/IntramembraneRegionFeatureRel.java]: ../model/relationships/features/IntramembraneRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/LipidMoietyBindingRegionFeatureRel.java]: ../model/relationships/features/LipidMoietyBindingRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/MetalIonBindingSiteFeatureRel.java]: ../model/relationships/features/MetalIonBindingSiteFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/ModifiedResidueFeatureRel.java]: ../model/relationships/features/ModifiedResidueFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/MutagenesisSiteFeatureRel.java]: ../model/relationships/features/MutagenesisSiteFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/NonConsecutiveResiduesFeatureRel.java]: ../model/relationships/features/NonConsecutiveResiduesFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/NonStandardAminoAcidFeatureRel.java]: ../model/relationships/features/NonStandardAminoAcidFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/NonTerminalResidueFeatureRel.java]: ../model/relationships/features/NonTerminalResidueFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/NucleotidePhosphateBindingRegionFeatureRel.java]: ../model/relationships/features/NucleotidePhosphateBindingRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/PeptideFeatureRel.java]: ../model/relationships/features/PeptideFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/PropeptideFeatureRel.java]: ../model/relationships/features/PropeptideFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/RegionOfInterestFeatureRel.java]: ../model/relationships/features/RegionOfInterestFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/RepeatFeatureRel.java]: ../model/relationships/features/RepeatFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/SequenceConflictFeatureRel.java]: ../model/relationships/features/SequenceConflictFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/SequenceVariantFeatureRel.java]: ../model/relationships/features/SequenceVariantFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/ShortSequenceMotifFeatureRel.java]: ../model/relationships/features/ShortSequenceMotifFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/SignalPeptideFeatureRel.java]: ../model/relationships/features/SignalPeptideFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/SiteFeatureRel.java]: ../model/relationships/features/SiteFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/SpliceVariantFeatureRel.java]: ../model/relationships/features/SpliceVariantFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/StrandFeatureRel.java]: ../model/relationships/features/StrandFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/TopologicalDomainFeatureRel.java]: ../model/relationships/features/TopologicalDomainFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/TransitPeptideFeatureRel.java]: ../model/relationships/features/TransitPeptideFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/TransmembraneRegionFeatureRel.java]: ../model/relationships/features/TransmembraneRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/TurnFeatureRel.java]: ../model/relationships/features/TurnFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/UnsureResidueFeatureRel.java]: ../model/relationships/features/UnsureResidueFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/features/ZincFingerRegionFeatureRel.java]: ../model/relationships/features/ZincFingerRegionFeatureRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/BiologicalProcessRel.java]: ../model/relationships/go/BiologicalProcessRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/CellularComponentRel.java]: ../model/relationships/go/CellularComponentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/HasPartOfGoRel.java]: ../model/relationships/go/HasPartOfGoRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/IsAGoRel.java]: ../model/relationships/go/IsAGoRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/MainGoRel.java]: ../model/relationships/go/MainGoRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/MolecularFunctionRel.java]: ../model/relationships/go/MolecularFunctionRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/NegativelyRegulatesGoRel.java]: ../model/relationships/go/NegativelyRegulatesGoRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/PartOfGoRel.java]: ../model/relationships/go/PartOfGoRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/PositivelyRegulatesGoRel.java]: ../model/relationships/go/PositivelyRegulatesGoRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/go/RegulatesGoRel.java]: ../model/relationships/go/RegulatesGoRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/InstituteCountryRel.java]: ../model/relationships/InstituteCountryRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/IsoformEventGeneratorRel.java]: ../model/relationships/IsoformEventGeneratorRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/ncbi/NCBIMainTaxonRel.java]: ../model/relationships/ncbi/NCBIMainTaxonRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/ncbi/NCBITaxonParentRel.java]: ../model/relationships/ncbi/NCBITaxonParentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/ncbi/NCBITaxonRel.java]: ../model/relationships/ncbi/NCBITaxonRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/BasicProteinSequenceCautionRel.java]: ../model/relationships/protein/BasicProteinSequenceCautionRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinDatasetRel.java]: ../model/relationships/protein/ProteinDatasetRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinEnzymaticActivityRel.java]: ../model/relationships/protein/ProteinEnzymaticActivityRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinErroneousGeneModelPredictionRel.java]: ../model/relationships/protein/ProteinErroneousGeneModelPredictionRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinErroneousInitiationRel.java]: ../model/relationships/protein/ProteinErroneousInitiationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinErroneousTerminationRel.java]: ../model/relationships/protein/ProteinErroneousTerminationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinErroneousTranslationRel.java]: ../model/relationships/protein/ProteinErroneousTranslationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinFrameshiftRel.java]: ../model/relationships/protein/ProteinFrameshiftRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinGenomeElementRel.java]: ../model/relationships/protein/ProteinGenomeElementRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinGoRel.java]: ../model/relationships/protein/ProteinGoRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinInterproRel.java]: ../model/relationships/protein/ProteinInterproRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinIsoformInteractionRel.java]: ../model/relationships/protein/ProteinIsoformInteractionRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinIsoformRel.java]: ../model/relationships/protein/ProteinIsoformRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinKeywordRel.java]: ../model/relationships/protein/ProteinKeywordRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinMiscellaneousDiscrepancyRel.java]: ../model/relationships/protein/ProteinMiscellaneousDiscrepancyRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinOrganismRel.java]: ../model/relationships/protein/ProteinOrganismRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinPfamRel.java]: ../model/relationships/protein/ProteinPfamRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinProteinInteractionRel.java]: ../model/relationships/protein/ProteinProteinInteractionRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinReactomeRel.java]: ../model/relationships/protein/ProteinReactomeRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/protein/ProteinSubcellularLocationRel.java]: ../model/relationships/protein/ProteinSubcellularLocationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementCDSRel.java]: ../model/relationships/refseq/GenomeElementCDSRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementGeneRel.java]: ../model/relationships/refseq/GenomeElementGeneRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementMiscRnaRel.java]: ../model/relationships/refseq/GenomeElementMiscRnaRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementMRnaRel.java]: ../model/relationships/refseq/GenomeElementMRnaRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementNcRnaRel.java]: ../model/relationships/refseq/GenomeElementNcRnaRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementRRnaRel.java]: ../model/relationships/refseq/GenomeElementRRnaRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementTmRnaRel.java]: ../model/relationships/refseq/GenomeElementTmRnaRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/refseq/GenomeElementTRnaRel.java]: ../model/relationships/refseq/GenomeElementTRnaRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/sc/ErroneousGeneModelPredictionRel.java]: ../model/relationships/sc/ErroneousGeneModelPredictionRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/sc/ErroneousInitiationRel.java]: ../model/relationships/sc/ErroneousInitiationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/sc/ErroneousTerminationRel.java]: ../model/relationships/sc/ErroneousTerminationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/sc/ErroneousTranslationRel.java]: ../model/relationships/sc/ErroneousTranslationRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/sc/FrameshiftRel.java]: ../model/relationships/sc/FrameshiftRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/sc/MiscellaneousDiscrepancyRel.java]: ../model/relationships/sc/MiscellaneousDiscrepancyRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/SubcellularLocationParentRel.java]: ../model/relationships/SubcellularLocationParentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/TaxonParentRel.java]: ../model/relationships/TaxonParentRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/uniref/UniRef100MemberRel.java]: ../model/relationships/uniref/UniRef100MemberRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/uniref/UniRef50MemberRel.java]: ../model/relationships/uniref/UniRef50MemberRel.java.md
[main/java/com/bio4j/neo4j/model/relationships/uniref/UniRef90MemberRel.java]: ../model/relationships/uniref/UniRef90MemberRel.java.md
[main/java/com/bio4j/neo4j/model/util/Bio4jManager.java]: ../model/util/Bio4jManager.java.md
[main/java/com/bio4j/neo4j/model/util/GoUtil.java]: ../model/util/GoUtil.java.md
[main/java/com/bio4j/neo4j/model/util/NodeIndexer.java]: ../model/util/NodeIndexer.java.md
[main/java/com/bio4j/neo4j/model/util/NodeRetriever.java]: ../model/util/NodeRetriever.java.md
[main/java/com/bio4j/neo4j/model/util/UniprotStuff.java]: ../model/util/UniprotStuff.java.md
[main/java/com/bio4j/neo4j/Neo4jManager.java]: ../Neo4jManager.java.md
[main/java/com/bio4j/neo4j/programs/GetProteinData.java]: GetProteinData.java.md
[main/java/com/bio4j/neo4j/programs/ImportEnzymeDB.java]: ImportEnzymeDB.java.md
[main/java/com/bio4j/neo4j/programs/ImportGeneOntology.java]: ImportGeneOntology.java.md
[main/java/com/bio4j/neo4j/programs/ImportIsoformSequences.java]: ImportIsoformSequences.java.md
[main/java/com/bio4j/neo4j/programs/ImportNCBITaxonomy.java]: ImportNCBITaxonomy.java.md
[main/java/com/bio4j/neo4j/programs/ImportNeo4jDB.java]: ImportNeo4jDB.java.md
[main/java/com/bio4j/neo4j/programs/ImportProteinInteractions.java]: ImportProteinInteractions.java.md
[main/java/com/bio4j/neo4j/programs/ImportRefSeq.java]: ImportRefSeq.java.md
[main/java/com/bio4j/neo4j/programs/ImportUniprot.java]: ImportUniprot.java.md
[main/java/com/bio4j/neo4j/programs/ImportUniref.java]: ImportUniref.java.md
[main/java/com/bio4j/neo4j/programs/IndexNCBITaxonomyByGiId.java]: IndexNCBITaxonomyByGiId.java.md
[main/java/com/bio4j/neo4j/programs/InitBio4jDB.java]: InitBio4jDB.java.md
[main/java/com/bio4j/neo4j/programs/UploadRefSeqSequencesToS3.java]: UploadRefSeqSequencesToS3.java.md
[main/java/com/ohnosequences/util/Executable.java]: ../../../ohnosequences/util/Executable.java.md
[main/java/com/ohnosequences/util/ExecuteFromFile.java]: ../../../ohnosequences/util/ExecuteFromFile.java.md
[main/java/com/ohnosequences/util/genbank/GBCommon.java]: ../../../ohnosequences/util/genbank/GBCommon.java.md
[main/java/com/ohnosequences/xml/api/interfaces/IAttribute.java]: ../../../ohnosequences/xml/api/interfaces/IAttribute.java.md
[main/java/com/ohnosequences/xml/api/interfaces/IElement.java]: ../../../ohnosequences/xml/api/interfaces/IElement.java.md
[main/java/com/ohnosequences/xml/api/interfaces/INameSpace.java]: ../../../ohnosequences/xml/api/interfaces/INameSpace.java.md
[main/java/com/ohnosequences/xml/api/interfaces/IXmlThing.java]: ../../../ohnosequences/xml/api/interfaces/IXmlThing.java.md
[main/java/com/ohnosequences/xml/api/interfaces/package-info.java]: ../../../ohnosequences/xml/api/interfaces/package-info.java.md
[main/java/com/ohnosequences/xml/api/model/NameSpace.java]: ../../../ohnosequences/xml/api/model/NameSpace.java.md
[main/java/com/ohnosequences/xml/api/model/package-info.java]: ../../../ohnosequences/xml/api/model/package-info.java.md
[main/java/com/ohnosequences/xml/api/model/XMLAttribute.java]: ../../../ohnosequences/xml/api/model/XMLAttribute.java.md
[main/java/com/ohnosequences/xml/api/model/XMLElement.java]: ../../../ohnosequences/xml/api/model/XMLElement.java.md
[main/java/com/ohnosequences/xml/api/model/XMLElementException.java]: ../../../ohnosequences/xml/api/model/XMLElementException.java.md
[main/java/com/ohnosequences/xml/api/util/XMLUtil.java]: ../../../ohnosequences/xml/api/util/XMLUtil.java.md
[main/java/com/ohnosequences/xml/model/bio4j/Bio4jNodeIndexXML.java]: ../../../ohnosequences/xml/model/bio4j/Bio4jNodeIndexXML.java.md
[main/java/com/ohnosequences/xml/model/bio4j/Bio4jNodeXML.java]: ../../../ohnosequences/xml/model/bio4j/Bio4jNodeXML.java.md
[main/java/com/ohnosequences/xml/model/bio4j/Bio4jPropertyXML.java]: ../../../ohnosequences/xml/model/bio4j/Bio4jPropertyXML.java.md
[main/java/com/ohnosequences/xml/model/bio4j/Bio4jRelationshipIndexXML.java]: ../../../ohnosequences/xml/model/bio4j/Bio4jRelationshipIndexXML.java.md
[main/java/com/ohnosequences/xml/model/bio4j/Bio4jRelationshipXML.java]: ../../../ohnosequences/xml/model/bio4j/Bio4jRelationshipXML.java.md
[main/java/com/ohnosequences/xml/model/bio4j/UniprotDataXML.java]: ../../../ohnosequences/xml/model/bio4j/UniprotDataXML.java.md
[main/java/com/ohnosequences/xml/model/go/GoAnnotationXML.java]: ../../../ohnosequences/xml/model/go/GoAnnotationXML.java.md
[main/java/com/ohnosequences/xml/model/go/GOSlimXML.java]: ../../../ohnosequences/xml/model/go/GOSlimXML.java.md
[main/java/com/ohnosequences/xml/model/go/GoTermXML.java]: ../../../ohnosequences/xml/model/go/GoTermXML.java.md
[main/java/com/ohnosequences/xml/model/go/SlimSetXML.java]: ../../../ohnosequences/xml/model/go/SlimSetXML.java.md
[main/java/com/ohnosequences/xml/model/uniprot/ArticleXML.java]: ../../../ohnosequences/xml/model/uniprot/ArticleXML.java.md
[main/java/com/ohnosequences/xml/model/uniprot/CommentXML.java]: ../../../ohnosequences/xml/model/uniprot/CommentXML.java.md
[main/java/com/ohnosequences/xml/model/uniprot/FeatureXML.java]: ../../../ohnosequences/xml/model/uniprot/FeatureXML.java.md
[main/java/com/ohnosequences/xml/model/uniprot/InterproXML.java]: ../../../ohnosequences/xml/model/uniprot/InterproXML.java.md
[main/java/com/ohnosequences/xml/model/uniprot/IsoformXML.java]: ../../../ohnosequences/xml/model/uniprot/IsoformXML.java.md
[main/java/com/ohnosequences/xml/model/uniprot/KeywordXML.java]: ../../../ohnosequences/xml/model/uniprot/KeywordXML.java.md
[main/java/com/ohnosequences/xml/model/uniprot/ProteinXML.java]: ../../../ohnosequences/xml/model/uniprot/ProteinXML.java.md
[main/java/com/ohnosequences/xml/model/uniprot/SubcellularLocationXML.java]: ../../../ohnosequences/xml/model/uniprot/SubcellularLocationXML.java.md
[main/java/com/ohnosequences/xml/model/util/Argument.java]: ../../../ohnosequences/xml/model/util/Argument.java.md
[main/java/com/ohnosequences/xml/model/util/Arguments.java]: ../../../ohnosequences/xml/model/util/Arguments.java.md
[main/java/com/ohnosequences/xml/model/util/Error.java]: ../../../ohnosequences/xml/model/util/Error.java.md
[main/java/com/ohnosequences/xml/model/util/Execution.java]: ../../../ohnosequences/xml/model/util/Execution.java.md
[main/java/com/ohnosequences/xml/model/util/FlexXMLWrapperClassCreator.java]: ../../../ohnosequences/xml/model/util/FlexXMLWrapperClassCreator.java.md
[main/java/com/ohnosequences/xml/model/util/ScheduledExecutions.java]: ../../../ohnosequences/xml/model/util/ScheduledExecutions.java.md
[main/java/com/ohnosequences/xml/model/util/XMLWrapperClass.java]: ../../../ohnosequences/xml/model/util/XMLWrapperClass.java.md
[main/java/com/ohnosequences/xml/model/util/XMLWrapperClassCreator.java]: ../../../ohnosequences/xml/model/util/XMLWrapperClassCreator.java.md