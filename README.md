## Backend Authentication

## Example 

```
#MS

PORT=5001
JDBC_URL=jdbc:postgresql://postgres-authentication/authenticationdb
IDLE_TIMEOUT=480000
JWK_PRIMARY="{\n    \"p\": \"72NKKXECCmBzmxEImTwClkPQf54jLj7nREU5Irsn9PIlRxQ16gYZZ4kOuH1VAZN_tG3I2Va_14ynmQymYA5_6SDTUbK3YN-lY1RaqoFrkQrbouZOZtqMV-pCF36t3EsXmwG94O2Uufp1TPXcqIXZPRx0DlrRz9MTLiBUk1PV4uE\", \"kty\": \"RSA\", \"q\": \"vZLcWrpKcpsHWQ8IYpk8RyOTzZxPIZf_HpvY6qWqKOh7Lh_NBElNP-mh2ldZu8cdg2fT5e54MHuo_SUmQK1MOtePMHJA0WMCvAGPbplfzwlpatQu7j1Ucog-PBR6Q8A2qShXvLjyKI9t-Vf0qmTrGtx0AyWg1M07LvcJN0MJ1LE\", \"d\": \"TJ_03lxrSAjDlHdnOzl9sfVJnvIFRnqURHOfL-68GIGsDbIatzTYBsqvEIigoUfLePqn5znr0FSkVKgfYPC4iDey7qmM6Vz-op3szdzQsqr7hYFM2VUNKzYUvBKpyi1poKRUwwHbsspKLNR2OysqvfJjgqEiSD6BNn2GDteYaiqiDDB-Vt99rVAVsf3ZrxR17CSyJeTTlWMdoDOORTef-fTxbyLrUwHRMrxOJKlM0_5MxaEzvWSejn6UgJxCg73txhdUUWwAoFeVNxbpwDO0yj5fIGsioTzHdusLaSfUMDj-oNstHz0U950SqmYW-9T7yd_TIVDDFQAUGXI6IUMQAQ\", \"e\": \"AQAB\", \"use\": \"sig\", \"kid\": \"h-jAkfaQSmqTnrdVMv7QuL_VIOLD_U9aEM02WX1-WVA\", \"qi\": \"kq5vuT8D6VJTVWyjR9ieC1CAS4-8Qik595zUOWpC3clcf9P7roMEi0MXsh-SJDn_v4q2ND89EncPYLwWe3rCvc09Iq3RwMnUqqFDi_5hTZbKM796bdrxxcbX0OUw4IjZMburZ2lnJnJQL-P8JqkiG62epMWEan26FdVcB9tZ19A\", \"dp\": \"RymC8FLRwnftpdkobzTxp-2821MNLkAwy5CwbWypch1Ri5GsfUcW4jpSD_HiBTftrJs-K62oFdGQOOUTyFZHG-xQJWc1MC0Loz1-PlV-vn010XUZNWYedjwDLazQzj3cZI3d_jFiFu8nU5t63vX7IDmHHoVM_bTHbNzbh3hSisE\", \"alg\": \"RS256\", \"dq\": \"S0rB4_LrUJtMDs4OqYJs02BI-rejIheJRvHk9ET50SeMjOQqfBJU8RZspxhcVl0d1qvtF_xflfn3Qxt3Fub9SL2GbFpkbgUh9-OywN1_lO4LBVfij9Kh8J82AIythq0XyJVyS001OHDG3-pUDTzpt5BpLzrJptsgL2YELgOdKvE\", \"n\": \"sUWibw7oZkNXF3Nq8xDH7fP1e6i0YggxuxnXsOOyRmVmcArGm925i1a8XCywfd8A8mQ1ziHcqulp2l_BnkG690EYRtF5REO9xfzQUaMDrrNHEkMcpypaFUjNMgX7My2nM_Zcdeb5UYgbGmAuaSGGxQ2IUr-g8XnCcvBs2EnOtclAJTzzbUkPIsoAEqa9637224XU4QHVLFeOgO106br04GHz1Iw-QDFo9AExBek9lSE8yTs7dk-is6FeTWxbnmCBabf75ckmcW8hkJVydFidS5rPG_cIt0kMRR9BCGC1evo_e1yFF3kdSQ6HOJHJyfr-9zQ9wdarClIPiA-G860xkQ\" \n}"
JWK_SECONDARY="{\n  \"p\": \"2hjYt43xKrl_h2HAh8NR6qAoxoGo3bRyQ4OFKpVRzGFZCd80qJRq3nzQyI1OR9NeahlMpPsLILC3hnlXRyU7uJSb9SqSBbkqt21oJXZtZvs24TSGGMmZuUFEtsYLpspQizA8_DSEr_4mfUtD2EWXwG02lECWdRsM_H44KPWBTu0\", \"kty\": \"RSA\", \"q\": \"q_z5wueJ6tP94nUj-wDHKcr085w9lbgJcc_7P_kP5XIukziFwIzlaO9sxnflRsojRebMTWqLkfm_eMV6LbIWORDdDdTeJHpEOm-ogn72fR-6qaaemOb3uqDUTlUPGuRT8s_O_F_LhVR2rxJYVVr_OZZ7Rm80RMNd7PRuB4D8-00\", \"d\": \"EC8-SxgOYbVXijC_5Bex16BHZckp8RqRqEqsHi0hjhzu6k64uFE5mmOBScVu2b3y0jyqEoubfW1YD3Qb7FahwutlWJ_3Y1wWlGLRE2lXQ0pCUpHd_gDy5x2IsHjgCOah3Z39XcXJR7E7SbxtLM_shBFNumyrW9BC227_HzkdP3I45AE0Z55PoM3wqndTJrfpd1ZUcCcFCS8Ri_m57lkOkGU3LXtM82nz9ei6DgYUoGH7nUqAy4138ojMhSI0ZFnjkUv4jKqosAYCC9PIoSFJf4wIIhYk13fJUmrhVOuAYqYfNnx7XyEm4LS0Yx8o5iY1NPwiLs3UQME2wYe-h08CcQ\", \"e\": \"AQAB\", \"use\": \"sig\", \"kid\": \"Aa10cU0LLpJ5mxQzpLf2cEsGTmtdOzeSZ80OvkdCaYQ\", \"qi\": \"z6HpLJaY6psPYDIaoqfdyclU0CLVMt0k99WnBq9dqgQWA5LG-URodKrb411ARXn1ay6XnY1lvb5K8FCLZq3XIwVlNRdRxluraRqoATqvkHxrTsVXiynb8sABw080ydeONGXoOyqFsMvtZwdPp4V_54-6Am8htpJPywvy5qL65rk\", \"dp\": \"tENFHLHdW4rmtWc-UvhFhL_L9AbHJDg5GURjlMbGTT9I8LfkNlI14p8Xkiv5P7ScnOU8TECS-foWNrekQ9Gqgu2R2hnNh518iQ-MvSodUrjgq1NWYrZ-sHgZ61fV2Y3u9JxyS6K86RoPhg_ni6I5w3qBWhfoQ_KP0UIh-PiyYCE\", \"alg\": \"RS256\", \"dq\": \"pdzzdc7cCBbn2tSDnZazfA-aSbHdBp2UpikgsiJc_TcvyZUSEI8Q0zFLot-PwsRAXjJlxydAcMf9qtgvqw_7NI-Zz7QBhAZR9FUomqkcx7Co0OG6Q9VDBb4C4YRQa-vVNe7JPESaPvwk9VPo0nBOgVgpjNTEAKi01UcpMZ1o3GU\", \"n\": \"koYeACdggAIGRzu7kKjTT63rxioS0rd-z3T2lnuNZNcMqzg6z8wDGS7YiyXBYGUpNKOIuYIa--RTxSkJGrgt4vjCgLNd3hpPBRs_If_h5GYwMfDQN_qoH2subuLXtmVPQnGfuxS5pH79zmjZ4tQEzyfrMkQsk2I6w6Dj9tBPhKM4St8eJxGaIuxCxs06ToPQmV1x66oFPJqkbHpvuNFoCaD9adw747TC6LXvu2bJg0CwOOVssAD8AsuN5sCJH9ODxuklQLEtKQaNfqMdVCCdlWR2zw8DcAQGTCBLypwzGZDcOzw844wUbd1lymi5bp_Le3MEsmjFEIJycF5C0ZMcSQ\" \n}"
KID=h-jAkfaQSmqTnrdVMv7QuL_VIOLD_U9aEM02WX1-WVA
KID2=Aa10cU0LLpJ5mxQzpLf2cEsGTmtdOzeSZ80OvkdCaYQ
JWT_EXPIRATIONS_SECONDS=86400
JWT_EXPIRATIONS_REFRESH_HOURS=168

#DB

DB_USER=root
DB_PASSWORD=root
DB_VOLUME_DIR=/home/luis/Documents/kraken-test/authentication
DB_NAME=authenticationdb
DB_PORT=5434
```