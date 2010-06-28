package edu.stanford.math.plex_plus.homology.mapping;

import edu.stanford.math.plex_plus.algebraic_structures.interfaces.IntRing;
import edu.stanford.math.plex_plus.datastructures.IntFormalSum;
import edu.stanford.math.plex_plus.homology.simplex.ChainBasisElement;
import edu.stanford.math.plex_plus.homology.simplex.HomProductPair;
import edu.stanford.math.plex_plus.homology.simplex.Simplex;
import edu.stanford.math.plex_plus.homology.simplex.TensorProductPair;
import edu.stanford.math.plex_plus.homology.utility.HomologyUtility;
import edu.stanford.math.plex_plus.utility.ExceptionUtility;
import gnu.trove.iterator.TObjectIntIterator;

public class MappingUtility {
	public static <T extends ChainBasisElement, U extends ChainBasisElement> IntFormalSum<U> computeImage(IntFormalSum<HomProductPair<T, U>> function, T element) {
		IntFormalSum<U> result = new IntFormalSum<U>();
		for (TObjectIntIterator<HomProductPair<T, U>> iterator = function.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			if (iterator.key().getFirst().equals(element)) {
				result.put(iterator.value(), iterator.key().getSecond());
			}
		}
		
		return result;
	}
	
	public static <T extends ChainBasisElement, U extends ChainBasisElement> IntFormalSum<HomProductPair<TensorProductPair<T, T>, TensorProductPair<U, U>>> functionTensorProduct(IntFormalSum<HomProductPair<T, U>> f, IntFormalSum<HomProductPair<T, U>> g) {
		IntFormalSum<HomProductPair<TensorProductPair<T, T>, TensorProductPair<U, U>>> result = new IntFormalSum<HomProductPair<TensorProductPair<T, T>, TensorProductPair<U, U>>>();
		
		for (TObjectIntIterator<HomProductPair<T, U>> f_iterator = f.iterator(); f_iterator.hasNext(); ) {
			f_iterator.advance();
			for (TObjectIntIterator<HomProductPair<T, U>> g_iterator = g.iterator(); g_iterator.hasNext(); ) {
				g_iterator.advance();
				TensorProductPair<T, T> source = new TensorProductPair<T, T>(f_iterator.key().getFirst(), g_iterator.key().getFirst());
				TensorProductPair<U, U> destination = new TensorProductPair<U, U>(f_iterator.key().getSecond(), g_iterator.key().getSecond());
				int coefficient = f_iterator.value() * g_iterator.value();
				result.put(coefficient, new HomProductPair<TensorProductPair<T, T>, TensorProductPair<U, U>>(source, destination));
			}
		}
		
		return result;
	}
	
	public static IntFormalSum<TensorProductPair<Simplex, Simplex>> alexanderWhitneyMap(Simplex element) {
		IntFormalSum<TensorProductPair<Simplex, Simplex>> result = new IntFormalSum<TensorProductPair<Simplex, Simplex>>();
		int[] vertices = element.getVertices();
		
		for (int i = 0; i < vertices.length; i++) {
			result.put(1, new TensorProductPair<Simplex, Simplex>(new Simplex(HomologyUtility.lowerEntries(vertices, i)), new Simplex(HomologyUtility.upperEntries(vertices, i))));
		}
		return result;
	}
	
	/*
	public static IntFormalSum<TensorProductPair<Simplex, Simplex>> alexanderWhitneyMap(IntFormalSum<Simplex> element) {
		IntFormalSum<TensorProductPair<Simplex, Simplex>> result = new IntFormalSum<TensorProductPair<Simplex, Simplex>>();
		
	}
	*/
	
	/**
	 * This function computes the quantity
	 * |\Delta (f (\sigma)) - f x f (\Delta (\sigma))| 
	 * 
	 * @param function
	 * @param sigma
	 * @return
	 */
	/*
	public static int alexanderWhitneyNorm(IntFormalSum<HomProductPair<Simplex, Simplex>> f, Simplex sigma) {
		IntFormalSum<TensorProductPair<Simplex, Simplex>> Delta_sigma = alexanderWhitneyMap(sigma);
		IntFormalSum<HomProductPair<TensorProductPair<Simplex, Simplex>, TensorProductPair<Simplex, Simplex>>> f_tensor_f = functionTensorProduct(f, f);
		
		IntFormalSum<TensorProductPair<Simplex, Simplex>> term1 = alexanderWhitneyMap(computeImage(f, sigma));
	}
	*/
	public static <T> int chainCardinality(IntFormalSum<T> chain) {
		return chain.size();
	}
	
	public static <T> int norm(IntFormalSum<T> chain, int p, IntRing ring) {
		ExceptionUtility.verifyNonNull(chain);
		ExceptionUtility.verifyNonNegative(p);
		
		if (p == 0) {
			return chain.size();
		} else {
			int norm = 0;
			for (TObjectIntIterator<T> iterator = chain.iterator(); iterator.hasNext(); ) {
				iterator.advance();
				norm += ring.power(Math.abs(iterator.value()), p);
			}
			return norm;
		}
	}
	
	public static <T> int infinityNorm(IntFormalSum<T> chain) {
		int norm = 0;
		for (TObjectIntIterator<T> iterator = chain.iterator(); iterator.hasNext(); ) {
			iterator.advance();
			norm = Math.max(norm, iterator.value());
		}
		return norm;
	}
	
	
}