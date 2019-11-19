(ns experiment.services.bcrypt
  (:import (at.favre.lib.crypto.bcrypt BCrypt BCrypt$Hasher BCrypt$Verifyer)))

(defn encrypt
  [password]
  (-> (BCrypt/withDefaults)
      (.hashToString 12 (.toCharArray password))))

(defn match?
  [encrypted, raw]
  (-> (BCrypt/verifyer)
      (.verify (.toCharArray raw) encrypted)
      .verified))
